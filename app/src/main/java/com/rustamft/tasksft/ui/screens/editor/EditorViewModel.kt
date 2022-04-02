package com.rustamft.tasksft.ui.screens.editor

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.rustamft.tasksft.R
import com.rustamft.tasksft.database.entity.ObservableTask
import com.rustamft.tasksft.database.entity.Task
import com.rustamft.tasksft.database.repository.TasksRepo
import com.rustamft.tasksft.utils.TASK_ID
import com.rustamft.tasksft.utils.datetime.DateTimeProvider
import com.rustamft.tasksft.utils.datetime.DateTimeString
import com.rustamft.tasksft.utils.displayToast
import com.rustamft.tasksft.utils.schedule.TasksWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val repo: TasksRepo,
    private val workManager: TasksWorkManager
) : ViewModel() {

    private var _task: Task? = null
    private val task get() = _task!! // Initial entity, it is not changed before saving.
    val observableTask = ObservableTask() // Temp task with observable fields.

    init {
        viewModelScope.launch {
            val id = state.get<Int>(TASK_ID)
            _task = if (id == null) {
                Task() // New empty task.
            } else {
                repo.getTask(id)
            }
            observableTask.fillFrom(task)
            observableTask.observeChanges()
        }
    }

    fun navigateBack(view: View) {
        val navController = view.findNavController()
        navController.popBackStack()
    }

    fun save(view: View) {
        with(view.context) {
            if (observableTask.title.get().isNullOrBlank()) { // If the entered title is empty.
                displayToast(getString(R.string.task_title_empty))
            } else { // If the entered title is valid.
                viewModelScope.launch {
                    updateTaskFromObservableTask()
                    launch {
                        workManager.cancel(task)
                        if (task.reminder != 0L) {
                            workManager.scheduleOneTime(task)
                            displayToast(buildUntilReminderString(this@with))
                        }
                    }
                    launch {
                        if (task.isNew) {
                            task.isNew = false
                            repo.insert(task)
                        } else { // If it's an existing task.
                            repo.update(task)
                        }
                    }
                    navigateBack(view)
                }
            }
        }
    }

    fun delete() {
        if (!task.isNew) {
            viewModelScope.launch {
                launch {
                    repo.delete(task)
                }
                workManager.cancel(task)
            }
        }
    }

    fun onBackClicked(view: View) {
        if (observableTask.isChanged) {
            displaySaveDialog(view)
        } else {
            navigateBack(view)
        }
    }

    private fun buildUntilReminderString(context: Context): String {
        with(context) {
            val dateTime = DateTimeProvider.dateTimeUntil(task.reminder)
            var string = getString(R.string.reminder_in)
            if (dateTime.month > 0) {
                string += dateTime.month.toString() + getString(R.string.reminder_months)
            }
            if (dateTime.day > 0) {
                string += dateTime.day.toString() + getString(R.string.reminder_days)
            }
            if (dateTime.hour > 0) {
                string += dateTime.hour.toString() + getString(R.string.reminder_hours)
            }
            if (dateTime.minute > 0) {
                string += dateTime.minute.toString() + getString(R.string.reminder_minutes)
            }
            return string
        }
    }

    private fun displaySaveDialog(view: View) {
        with(view.context) {
            val builder = AlertDialog.Builder(this)
                .setTitle(getString(R.string.unsaved_changes))
                .setMessage(view.context.getString(R.string.what_to_do))
                .setPositiveButton(getString(R.string.action_save)) { _, _ ->
                    save(view)
                }
                .setNegativeButton(getString(R.string.action_cancel)) { _, _ ->
                    // Close the dialog.
                }
                .setNeutralButton(getString(R.string.action_discard)) { _, _ ->
                    navigateBack(view)
                }
            builder.show()
        }
    }

    private fun updateTaskFromObservableTask() {
        with(task) {
            title = observableTask.title.get()!!
            description = observableTask.description.get()!!
            isFinished = false
            reminder = if (!observableTask.hasReminder.get()!!) { // If reminder is not needed.
                0L
            } else { // If reminder should be set.
                val dateTime = DateTimeString(
                    observableTask.date.get()!!,
                    observableTask.time.get()!!
                )
                DateTimeProvider.stringToMillis(dateTime)
            }
        }
    }
}
