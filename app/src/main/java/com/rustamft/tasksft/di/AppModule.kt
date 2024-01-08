package com.rustamft.tasksft.di

import android.os.Bundle
import android.util.Log
import com.rustamft.tasksft.presentation.global.SnackbarFlow
import com.rustamft.tasksft.presentation.global.TAG_COROUTINE_EXCEPTION
import com.rustamft.tasksft.presentation.model.UIText
import com.rustamft.tasksft.presentation.screen.editor.EditorViewModel
import com.rustamft.tasksft.presentation.screen.list.ListViewModel
import com.rustamft.tasksft.presentation.screen.settings.SettingsViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<SnackbarFlow> {
        SnackbarFlow()
    }

    single<CoroutineExceptionHandler> {
        val snackbarFlow: SnackbarFlow = get()
        CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG_COROUTINE_EXCEPTION, Log.getStackTraceString(throwable))
            snackbarFlow.tryEmit(UIText.DynamicString(throwable.message.toString()))
        }
    }

    viewModel<ListViewModel> {
        ListViewModel(
            getAllTasksUseCase = get(),
            saveTaskUseCase = get(),
            deleteTasksUseCase = get(),
            exceptionHandler = get()
        )
    }

    viewModel<EditorViewModel> { (bundle: Bundle) ->
        EditorViewModel(
            arguments = bundle,
            getTaskUseCase = get(),
            saveTaskUseCase = get(),
            deleteTaskUseCase = get(),
            snackbarFlow = get(),
            exceptionHandler = get()
        )
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(
            getPreferencesUseCase = get(),
            savePreferencesUseCase = get(),
            exportTasksUseCase = get(),
            importTasksUseCase = get(),
            snackbarFlow = get(),
            exceptionHandler = get()
        )
    }
}
