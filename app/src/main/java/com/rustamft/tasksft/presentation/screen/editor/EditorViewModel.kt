package com.rustamft.tasksft.presentation.screen.editor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustamft.tasksft.domain.usecase.GetListOfTasksUseCase
import com.rustamft.tasksft.presentation.screen.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getListOfTasksUseCase: GetListOfTasksUseCase
) : ViewModel() {

    private val navArgs = savedStateHandle.navArgs<EditorScreenNavArgs>()
    private val taskFlow = if (navArgs.indexOfTaskInList == -1) {
        emptyFlow()
    } else {
        getListOfTasksUseCase.execute().map { it[navArgs.indexOfTaskInList] }
    }
    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var reminder by mutableStateOf(0L)

    init {
        viewModelScope.launch {
            withTimeout(3000) {
                taskFlow.collect { task ->
                    title = task.title
                    description = task.description
                }
            }
        }
    }
}
