package com.rustamft.tasksft.presentation.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.usecase.GetListOfTasksUseCase
import com.rustamft.tasksft.domain.usecase.SaveTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    getListOfTasksUseCase: GetListOfTasksUseCase,
    private val saveTaskUseCase: SaveTaskUseCase
) : ViewModel() {

    val listOfTasksFlow = getListOfTasksUseCase.execute().map { list ->
        list.sortedWith(
            compareBy(
                { it.isFinished },
                { it.reminder },
                { it.created }
            )
        )
    }

    fun saveTask(task: Task) {
        viewModelScope.launch {
            saveTaskUseCase.execute(task = task)
        }
    }
}