package com.rustamft.tasksft.di

import android.os.Bundle
import com.rustamft.tasksft.presentation.screen.backup.BackupViewModel
import com.rustamft.tasksft.presentation.screen.editor.EditorViewModel
import com.rustamft.tasksft.presentation.screen.list.ListViewModel
import kotlinx.coroutines.channels.Channel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<Channel<String>> {
        Channel()
    }

    viewModel<ListViewModel> {
        ListViewModel(
            getListOfTasksUseCase = get(),
            saveTaskUseCase = get(),
            deleteTasksUseCase = get(),
            snackbarChannel = get()
        )
    }

    viewModel<EditorViewModel> { (bundle: Bundle) ->
        EditorViewModel(
            arguments = bundle,
            getTaskByIdUseCase = get(),
            saveTaskUseCase = get(),
            deleteTaskUseCase = get(),
            snackbarChannel = get()
        )
    }

    viewModel<BackupViewModel> {
        BackupViewModel(
            getAppPreferencesUseCase = get(),
            saveAppPreferencesUseCase = get(),
            exportTasksUseCase = get(),
            importTasksUseCase = get(),
            snackbarChannel = get()
        )
    }
}
