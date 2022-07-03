package com.rustamft.tasksft.presentation.screen.editor

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustamft.tasksft.domain.usecase.DeleteTaskUseCase
import com.rustamft.tasksft.domain.usecase.GetTaskByIdUseCase
import com.rustamft.tasksft.domain.usecase.SaveTaskUseCase
import com.rustamft.tasksft.domain.util.TASK_ID
import com.rustamft.tasksft.domain.util.toTimeUntil
import com.rustamft.tasksft.presentation.model.MutableTask
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class EditorViewModel(
    arguments: Bundle,
    getTaskByIdUseCase: GetTaskByIdUseCase,
    private val saveTaskUseCase: SaveTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val snackbarChannel: Channel<String>
) : ViewModel() {

    private val successChannel = Channel<Boolean>()
    val successFlow = successChannel.receiveAsFlow()

    private val taskId = arguments.getInt(TASK_ID)
    private val taskFlow = if (taskId == 0) {
        emptyFlow()
    } else {
        getTaskByIdUseCase.execute(taskId = taskId)
    }

    val mutableTask = MutableTask()

    init {
        viewModelScope.launch {
            withTimeout(2000) {
                taskFlow.collect { task ->
                    if (task != null) {
                        mutableTask.setFieldsFromTask(task = task)
                    }
                }
            }
        }
    }

    fun saveTask() {
        viewModelScope.launch {
            kotlin.runCatching {
                saveTaskUseCase.execute(task = mutableTask.toTask())
            }.onSuccess {
                val timeUntil = mutableTask.reminderCalendar.timeInMillis.toTimeUntil()
                snackbarChannel.send() // TODO: show actual time, maybe use UIText
                successChannel.send(true)
            }.onFailure {
                snackbarChannel.send(it.message.toString())
            }
        }
    }

    fun deleteTask() {
        viewModelScope.launch {
            kotlin.runCatching {
                deleteTaskUseCase.execute(task = mutableTask.toTask())
            }.onSuccess {
                successChannel.send(true)
            }.onFailure {
                snackbarChannel.send(it.message.toString())
            }
        }
    }
}
