package com.rustamft.tasksft.presentation.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.usecase.DeleteTasksUseCase
import com.rustamft.tasksft.domain.usecase.GetListOfTasksUseCase
import com.rustamft.tasksft.domain.usecase.SaveTaskUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ListViewModel(
    getListOfTasksUseCase: GetListOfTasksUseCase,
    private val saveTaskUseCase: SaveTaskUseCase,
    private val deleteTasksUseCase: DeleteTasksUseCase
) : ViewModel() {

    private val messageChannel = Channel<String>()
    val messageFlow = messageChannel.receiveAsFlow()

    val listOfTasksFlow = getListOfTasksUseCase.execute()

    fun saveTask(task: Task) {
        viewModelScope.launch {
            kotlin.runCatching {
                saveTaskUseCase.execute(task = task)
            }.onFailure {
                sendMessage(it.message.toString())
            }
        }
    }

    fun deleteTasks(list: List<Task>) {
        viewModelScope.launch {
            kotlin.runCatching {
                deleteTasksUseCase.execute(list = list)
            }.onFailure {
                sendMessage(it.message.toString())
            }
        }
    }

    private suspend fun sendMessage(message: String) {
        messageChannel.send(message)
    }
}
