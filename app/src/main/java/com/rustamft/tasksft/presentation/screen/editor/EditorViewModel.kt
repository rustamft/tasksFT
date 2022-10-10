package com.rustamft.tasksft.presentation.screen.editor

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.usecase.DeleteTaskUseCase
import com.rustamft.tasksft.domain.usecase.GetTaskByIdUseCase
import com.rustamft.tasksft.domain.usecase.SaveTaskUseCase
import com.rustamft.tasksft.domain.util.TASK_ID
import com.rustamft.tasksft.domain.util.toTimeDifference
import com.rustamft.tasksft.presentation.util.TaskStateHolder
import com.rustamft.tasksft.presentation.util.UIText
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class EditorViewModel(
    arguments: Bundle,
    getTaskByIdUseCase: GetTaskByIdUseCase,
    private val saveTaskUseCase: SaveTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val snackbarChannel: Channel<UIText>,
    private val exceptionHandler: CoroutineExceptionHandler,
) : ViewModel() {

    private val taskId: Int = arguments.getInt(TASK_ID)
    private val successChannel = Channel<Boolean>()
    val successFlow = successChannel.receiveAsFlow()
    val taskStateHolder: TaskStateHolder = TaskStateHolder()

    init {
        viewModelScope.launch(exceptionHandler) {
            val task = getTaskByIdUseCase.execute(taskId = taskId).first()
            taskStateHolder.setStateFromTask(task = task)
        }
    }

    fun saveTask() {
        launchInViewModelScope(
            successMessage = if (taskStateHolder.reminderIsSet) {
                val difference = taskStateHolder.reminderCalendar.timeInMillis.toTimeDifference()
                UIText.StringResource(
                    R.string.reminder_in,
                    difference.months,
                    difference.days,
                    difference.hours,
                    difference.minutes
                )
            } else null
        ) {
            saveTaskUseCase.execute(task = taskStateHolder.getStateAsTask())
        }
    }

    fun deleteTask() {
        launchInViewModelScope {
            deleteTaskUseCase.execute(task = taskStateHolder.getStateAsTask())
        }
    }

    private fun launchInViewModelScope(
        successMessage: UIText? = null,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(exceptionHandler) {
            launch { block() }.join()
            if (successMessage != null) {
                snackbarChannel.send(successMessage)
            }
            successChannel.send(true)
        }
    }
}
