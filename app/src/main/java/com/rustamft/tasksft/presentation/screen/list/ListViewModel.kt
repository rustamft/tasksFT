package com.rustamft.tasksft.presentation.screen.list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.usecase.DeleteTaskUseCase
import com.rustamft.tasksft.domain.usecase.GetAllTasksUseCase
import com.rustamft.tasksft.domain.usecase.SaveTaskUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ListViewModel(
    getAllTasksUseCase: GetAllTasksUseCase,
    private val saveTaskUseCase: SaveTaskUseCase,
    private val deleteTasksUseCase: DeleteTaskUseCase,
    private val exceptionHandler: CoroutineExceptionHandler
) : ViewModel() {

    val listOfTasksFlow = getAllTasksUseCase.execute()
    val openAppInfoDialogState = mutableStateOf(false)
    val openGitHubState = mutableStateOf(false)

    fun saveTask(task: Task) {
        launchInViewModelScope { saveTaskUseCase.execute(task = task) }
    }

    fun deleteTasks(tasks: List<Task>) {
        launchInViewModelScope { deleteTasksUseCase.execute(tasks = tasks) }
    }

    private fun launchInViewModelScope(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(exceptionHandler) { block() }
    }
}
