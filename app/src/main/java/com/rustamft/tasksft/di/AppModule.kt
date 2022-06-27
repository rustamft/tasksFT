package com.rustamft.tasksft.di

import com.rustamft.tasksft.presentation.screen.editor.EditorViewModel
import com.rustamft.tasksft.presentation.screen.list.ListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel<ListViewModel> {
        ListViewModel(
            getListOfTasksUseCase = get(),
            saveTaskUseCase = get(),
            deleteTasksUseCase = get()
        )
    }

    viewModel<EditorViewModel> {
        EditorViewModel(
            savedStateHandle = get(),
            getTaskByIdUseCase = get(),
            saveTaskUseCase = get()
        )
    }
}
