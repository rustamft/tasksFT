package com.rustamft.tasksft.screens.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustamft.tasksft.database.entity.AppTask
import com.rustamft.tasksft.database.entity.ObservableTask
import com.rustamft.tasksft.database.repository.AppRepo
import com.rustamft.tasksft.utils.Constants
import com.rustamft.tasksft.utils.datetime.DateTimeString
import com.rustamft.tasksft.utils.datetime.DateTimeUtil
import com.rustamft.tasksft.utils.schedule.AppWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val repo: AppRepo,
    private val workManager: AppWorkManager
) : ViewModel() {

    private var _task: AppTask? = null
    private val task get() = _task!! // Initial entity, it is not changed before saving.
    val observableTask = ObservableTask() // Temp task with observable fields.

    init {
        viewModelScope.launch {
            val id = state.get<Int>(Constants.TASK_ID)
            withContext(viewModelScope.coroutineContext) {
                _task = if (id == null || id == -1) {
                    AppTask()
                } else {
                    repo.getEntity(id)
                }
            }
            withContext(viewModelScope.coroutineContext) {
                observableTask.fillFrom(task)
            }
            observableTask.observeChanges()
        }
    }

    @Throws(Exception::class)
    suspend fun save() {
        if (observableTask.title.get().isNullOrBlank()) { // If the entered title is empty.
            throw Exception("Title is empty")
        } else { // If the entered title is valid.
            if (task.millis != 0L) {
                workManager.cancel(task)
            }
            task.title = observableTask.title.get()!!
            task.description = observableTask.description.get()!!
            task.isFinished = false
            if (task.id == -1) { // If it's a new task.
                val ids = repo.getIds() // Get the list of all entity ids in database.
                var i = 0
                while (ids.indexOf(i) != -1) {
                    i++
                }
                task.id = i // A non-existing id.
                repo.save(task)
            } else { // If it's an existing task.
                repo.update(task)
            }
            if (observableTask.hasReminder.get()!!) { // If reminder should be set.
                val dateTime = DateTimeString(
                    observableTask.date.get()!!,
                    observableTask.time.get()!!
                )
                task.millis = DateTimeUtil.stringToMillis(dateTime)
                workManager.scheduleOneTime(task)
            } else { // If reminder is not needed.
                task.millis = 0L
            }
        }
    }

    fun delete() {
        viewModelScope.launch {
            repo.delete(task)
            workManager.cancel(task)
        }
    }

    fun isTaskChanged() = observableTask.isChanged

    fun hasTaskReminder() = observableTask.hasReminder.get()!!

    fun dateTimeUntilReminder() = DateTimeUtil.dateTimeUntil(task.millis)
}
