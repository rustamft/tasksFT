package com.rustamft.tasksft.screens.editor

import androidx.lifecycle.SavedStateHandle
import com.rustamft.tasksft.database.entity.AppTask
import com.rustamft.tasksft.database.repository.TasksRoomRepoMock
import com.rustamft.tasksft.utils.Const
import com.rustamft.tasksft.utils.datetime.DateTimeUtil
import com.rustamft.tasksft.utils.TaskWorkManagerMock
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue

// https://kotlinlang.org/docs/jvm-test-using-junit.html#run-a-test
@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class EditorViewModelTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private var _state: SavedStateHandle? = null
    private val state get() = _state

    private var _repo: TasksRoomRepoMock? = null
    private val repo get() = _repo

    private var _workManager: TaskWorkManagerMock? = null
    private val workManager get() = _workManager

    private var _editorViewModel: EditorViewModel? = null
    private val editorViewModel get() = _editorViewModel!!

    @BeforeAll
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @AfterAll
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @BeforeEach
    fun init() {

        _state = SavedStateHandle(mapOf(Pair<String, AppTask?>(Const.TASK_ID, null)))
        _repo = TasksRoomRepoMock()
        _workManager = TaskWorkManagerMock()

        val id = 0

        state!!.set(Const.TASK_ID, id)

        _editorViewModel = EditorViewModel(
            state!!,
            repo!!,
            workManager!!
        )
    }

    @Test
    fun saveThrowsExceptionIfTitleEmpty() {

        runBlocking {
            coroutineScope {
                launch {

                    editorViewModel.observableTask.title.set("")
                    var message = ""

                    val deferred = coroutineScope {
                        async {
                            try {
                                editorViewModel.save()
                            } catch (e: Exception) {
                                message = e.message.toString()
                            }
                        }
                    }
                    deferred.await()

                    assertTrue(message == "Title is empty")

                }
            }
        }
    }

    @Test
    fun saveNotificationSetProperly() {
        runBlocking {
            coroutineScope {
                launch {

                    val dateString = "01 Jan 2025"
                    val timeString = "01:00"
                    var millisToSchedule = 0L

                    val deferred1 = coroutineScope {
                        async {
                            millisToSchedule =
                                DateTimeUtil.stringToMillis("$dateString $timeString", 2)

                            editorViewModel.observableTask.hasReminder.set(true)
                            editorViewModel.observableTask.date.set(dateString)
                            editorViewModel.observableTask.time.set(timeString)
                        }
                    }
                    deferred1.await()

                    val deferred2 = coroutineScope {
                        async {
                            try {
                                editorViewModel.save()
                            } catch (e: Exception) {
                                print("PRINT_EXCEPTION: ${e.message}")
                            }
                        }
                    }
                    deferred2.await()

                    assertTrue(workManager!!.scheduledMillis == millisToSchedule)

                }
            }
        }
    }
}