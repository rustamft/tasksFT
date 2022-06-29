package com.rustamft.tasksft.di

import android.os.Bundle
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

    viewModel<EditorViewModel> { (bundle: Bundle) ->
        EditorViewModel(
            arguments = bundle,
            getTaskByIdUseCase = get(),
            saveTaskUseCase = get()
        )
    }
}
