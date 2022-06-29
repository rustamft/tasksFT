package com.rustamft.tasksft.presentation.screen.editor

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustamft.tasksft.domain.usecase.GetTaskByIdUseCase
import com.rustamft.tasksft.domain.usecase.SaveTaskUseCase
import com.rustamft.tasksft.domain.util.TASK_ID
import com.rustamft.tasksft.presentation.model.MutableTask
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class EditorViewModel(
    arguments: Bundle,
    getTaskByIdUseCase: GetTaskByIdUseCase,
    private val saveTaskUseCase: SaveTaskUseCase
) : ViewModel() {

    private val messageChannel = Channel<String>()
    val messageFlow = messageChannel.receiveAsFlow()

    private val taskId = arguments.getInt(TASK_ID)
    private val taskFlow = if (taskId == 0) {
        emptyFlow()
    } else {
        getTaskByIdUseCase.execute(taskId = taskId)
    }

    val mutableTask = MutableTask()

    init {
        viewModelScope.launch {
            withTimeout(5000) {
                taskFlow.collect { task ->
                    mutableTask.setFieldsFromTask(task = task)
                }
            }
        }
    }

    fun saveTask() {
        viewModelScope.launch {
            kotlin.runCatching {
                saveTaskUseCase.execute(task = mutableTask.toTask())
            }.onSuccess {
                messageChannel.send("Reminder set") // TODO: show actual time
            }.onFailure {
                sendMessage(it.message.toString())
            }
        }
    }

    private suspend fun sendMessage(message: String) {
        messageChannel.send(message)
    }
}
