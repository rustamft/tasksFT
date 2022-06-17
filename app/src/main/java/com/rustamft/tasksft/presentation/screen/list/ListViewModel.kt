package com.rustamft.tasksft.presentation.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.usecase.DeleteTasksUseCase
import com.rustamft.tasksft.domain.usecase.GetListOfTasksUseCase
import com.rustamft.tasksft.domain.usecase.SaveTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    getListOfTasksUseCase: GetListOfTasksUseCase,
    private val saveTaskUseCase: SaveTaskUseCase,
    private val deleteTasksUseCase: DeleteTasksUseCase
) : ViewModel() {

    private val errorChannel = Channel<String>()
    val errorFlow = errorChannel.receiveAsFlow()

    val listOfTasksFlow = getListOfTasksUseCase.execute().map { list ->
        list.sortedWith(
            compareBy(
                { !it.isFinished },
                { it.reminder },
                { it.created }
            )
        )
    }

    fun saveTask(task: Task) {
        viewModelScope.launch {
            kotlin.runCatching {
                saveTaskUseCase.execute(task = task)
            }.onFailure {
                errorChannel.send(it.message.toString())
            }
        }
    }

    fun deleteTasks(list: List<Task>) {
        viewModelScope.launch {
            kotlin.runCatching {
                deleteTasksUseCase.execute(list = list)
            }.onFailure {
                errorChannel.send(it.message.toString())
            }
        }
    }
}
