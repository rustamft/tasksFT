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
            _task = if (id == null || id == -1) {
                AppTask()
            } else {
                repo.getEntity(id)
            }
            observableTask.fillFrom(task)
            observableTask.observeChanges()
        }
    }

    @Throws(Exception::class)
    suspend fun save() {
        if (observableTask.title.get().isNullOrBlank()) { // If the entered title is empty.
            throw Exception("Title is empty")
        } else { // If the entered title is valid.
            val taskIsNew = task.id == -1
            updateTaskFromObservableTask()
            workManager.cancel(task)
            if (task.millis != 0L) {
                workManager.scheduleOneTime(task)
            }
            if (taskIsNew) {
                repo.save(task)
            } else { // If it's an existing task.
                repo.update(task)
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

    private suspend fun updateTaskFromObservableTask() {
        with(task) {
            if (id == -1) {
                id = repo.getNonExistingId()
            }
            title = observableTask.title.get()!!
            description = observableTask.description.get()!!
            isFinished = false
            millis = if (observableTask.hasReminder.get()!!) { // If reminder should be set.
                val dateTime = DateTimeString(
                    observableTask.date.get()!!,
                    observableTask.time.get()!!
                )
                DateTimeUtil.stringToMillis(dateTime)
            } else { // If reminder is not needed.
                0L
            }
        }
    }
}
