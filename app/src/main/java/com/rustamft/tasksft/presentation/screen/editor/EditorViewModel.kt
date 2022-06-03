package com.rustamft.tasksft.presentation.screen.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.rustamft.tasksft.domain.usecase.GetListOfTasksUseCase
import com.rustamft.tasksft.presentation.screen.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getListOfTasksUseCase: GetListOfTasksUseCase
) : ViewModel() {

    private val navArgs = savedStateHandle.navArgs<EditorScreenNavArgs>()
    val taskFlow = if (navArgs.indexOfTaskInList == -1) {
        emptyFlow()
    } else {
        getListOfTasksUseCase.execute().map { it[navArgs.indexOfTaskInList] }
    }
}
