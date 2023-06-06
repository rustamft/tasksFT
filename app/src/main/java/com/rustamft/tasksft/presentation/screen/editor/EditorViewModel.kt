package com.rustamft.tasksft.presentation.screen.editor

import android.os.Bundle
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.usecase.DeleteTaskUseCase
import com.rustamft.tasksft.domain.usecase.GetTaskUseCase
import com.rustamft.tasksft.domain.usecase.SaveTaskUseCase
import com.rustamft.tasksft.presentation.global.TASK_ID
import com.rustamft.tasksft.presentation.global.toTimeDifference
import com.rustamft.tasksft.presentation.model.TaskViewState
import com.rustamft.tasksft.presentation.model.UIText
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class EditorViewModel(
    arguments: Bundle,
    getTaskUseCase: GetTaskUseCase,
    private val saveTaskUseCase: SaveTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val snackbarChannel: Channel<UIText>,
    private val exceptionHandler: CoroutineExceptionHandler,
) : ViewModel() {

    private val taskId = arguments.getInt(TASK_ID)
    private val successChannel = Channel<Boolean>()
    val successFlow = successChannel.receiveAsFlow()
    val taskViewState = TaskViewState()
    val openTaskInfoDialogState = mutableStateOf(false)
    val openChooseColorDialogState = mutableStateOf(false)
    val openUnsavedTaskDialogState = mutableStateOf(false)
    val valueChangedState = mutableStateOf(false)

    init {
        viewModelScope.launch(exceptionHandler) {
            val task = getTaskUseCase.execute(taskId = taskId).first() ?: return@launch
            taskViewState.setStateFromTask(task = task)
        }
    }

    fun saveTask() {
        launchInViewModelScope(
            successMessage = if (taskViewState.isReminderSet) null else {
                val difference = taskViewState.reminder.timeInMillis.toTimeDifference()
                UIText.StringResource(
                    R.string.reminder_in,
                    difference.days,
                    difference.hours,
                    difference.minutes
                )
            }
        ) {
            saveTaskUseCase.execute(task = taskViewState.getStateAsTask())
        }
    }

    fun deleteTask() {
        launchInViewModelScope {
            deleteTaskUseCase.execute(task = taskViewState.getStateAsTask())
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
