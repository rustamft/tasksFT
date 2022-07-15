package com.rustamft.tasksft.di

import android.os.Bundle
import com.rustamft.tasksft.presentation.activity.MainViewModel
import com.rustamft.tasksft.presentation.screen.settings.SettingsViewModel
import com.rustamft.tasksft.presentation.screen.editor.EditorViewModel
import com.rustamft.tasksft.presentation.screen.list.ListViewModel
import com.rustamft.tasksft.presentation.util.UIText
import kotlinx.coroutines.channels.Channel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<Channel<UIText>> {
        Channel()
    }

    viewModel<MainViewModel> {
        MainViewModel(getAppPreferencesUseCase = get())
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

    viewModel<SettingsViewModel> {
        SettingsViewModel(
            getAppPreferencesUseCase = get(),
            saveAppPreferencesUseCase = get(),
            exportTasksUseCase = get(),
            importTasksUseCase = get(),
            snackbarChannel = get()
        )
    }
}
