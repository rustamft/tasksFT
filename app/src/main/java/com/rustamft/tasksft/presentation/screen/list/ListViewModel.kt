package com.rustamft.tasksft.presentation.screen.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.usecase.DeleteTaskUseCase
import com.rustamft.tasksft.domain.usecase.GetListOfTasksUseCase
import com.rustamft.tasksft.domain.usecase.SaveTaskUseCase
import com.rustamft.tasksft.presentation.util.UIText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class ListViewModel(
    getListOfTasksUseCase: GetListOfTasksUseCase,
    private val saveTaskUseCase: SaveTaskUseCase,
    private val deleteTasksUseCase: DeleteTaskUseCase,
    private val snackbarChannel: Channel<UIText>
) : ViewModel() {

    private var listOfTasksState = mutableStateOf(emptyList<Task>())
    val listOfTasks by listOfTasksState

    init {
        viewModelScope.launch {
            getListOfTasksUseCase.execute().collect { list ->
                listOfTasksState.value = list
            }
        }
    }

    fun saveTask(task: Task) {
        viewModelScope.launch {
            kotlin.runCatching {
                saveTaskUseCase.execute(task = task)
            }.onFailure {
                snackbarChannel.send(UIText.DynamicString(it.message.toString()))
            }
        }
    }

    fun deleteTasks(list: List<Task>) {
        viewModelScope.launch {
            kotlin.runCatching {
                deleteTasksUseCase.execute(list = list)
            }.onFailure {
                snackbarChannel.send(UIText.DynamicString(it.message.toString()))
            }
        }
    }
}
