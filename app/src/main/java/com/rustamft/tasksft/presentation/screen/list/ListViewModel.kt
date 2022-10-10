package com.rustamft.tasksft.presentation.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.usecase.DeleteTaskUseCase
import com.rustamft.tasksft.domain.usecase.GetListOfTasksUseCase
import com.rustamft.tasksft.domain.usecase.SaveTaskUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ListViewModel(
    getListOfTasksUseCase: GetListOfTasksUseCase,
    private val saveTaskUseCase: SaveTaskUseCase,
    private val deleteTasksUseCase: DeleteTaskUseCase,
    private val exceptionHandler: CoroutineExceptionHandler
) : ViewModel() {

    val listOfTasksFlow = getListOfTasksUseCase.execute()

    fun saveTask(task: Task) {
        launchInViewModelScope { saveTaskUseCase.execute(task = task) }
    }

    fun deleteTasks(list: List<Task>) {
        launchInViewModelScope { deleteTasksUseCase.execute(list = list) }
    }

    private fun launchInViewModelScope(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(exceptionHandler) { block() }
    }
}
