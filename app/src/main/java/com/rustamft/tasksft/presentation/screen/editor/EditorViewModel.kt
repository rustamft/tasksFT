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
    private val taskId: Int = arguments.getInt(TASK_ID),
    val taskStateHolder: TaskStateHolder = TaskStateHolder()
) : ViewModel() {

    private val successChannel = Channel<Boolean>()
    val successFlow = successChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val task = getTaskByIdUseCase.execute(taskId = taskId).first()
            if (task != null) {
                taskStateHolder.setStateFromTask(task = task)
            }
        }
    }

    fun saveTask() {
        viewModelScope.launch {
            kotlin.runCatching {
                saveTaskUseCase.execute(task = taskStateHolder.getStateAsTask())
            }.onSuccess {
                if (taskStateHolder.reminderIsSet) {
                    with(taskStateHolder.reminderCalendar.timeInMillis.toTimeDifference()) {
                        snackbarChannel.send(
                            UIText.StringResource(
                                R.string.reminder_in,
                                months,
                                days,
                                hours,
                                minutes
                            )
                        )
                    }
                }
                successChannel.send(true)
            }.onFailure {
                snackbarChannel.send(UIText.DynamicString(it.message.toString()))
            }
        }
    }

    fun deleteTask() {
        viewModelScope.launch {
            kotlin.runCatching {
                deleteTaskUseCase.execute(task = taskStateHolder.getStateAsTask())
            }.onSuccess {
                successChannel.send(true)
            }.onFailure {
                snackbarChannel.send(UIText.DynamicString(it.message.toString()))
            }
        }
    }
}
