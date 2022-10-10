package com.rustamft.tasksft.di

import android.os.Bundle
import android.util.Log
import com.rustamft.tasksft.presentation.util.TAG_COROUTINE_EXCEPTION
import com.rustamft.tasksft.presentation.screen.editor.EditorViewModel
import com.rustamft.tasksft.presentation.screen.list.ListViewModel
import com.rustamft.tasksft.presentation.screen.settings.SettingsViewModel
import com.rustamft.tasksft.presentation.util.model.UIText
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onSuccess
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<Channel<UIText>> {
        Channel()
    }

    single<CoroutineExceptionHandler> {
        val snackbarChannel: Channel<UIText> = get()
        CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG_COROUTINE_EXCEPTION, Log.getStackTraceString(throwable))
            repeat(3) {
                snackbarChannel.trySend(UIText.DynamicString(throwable.message.toString()))
                    .onSuccess { return@repeat }
            }
        }
    }

    viewModel<ListViewModel> {
        ListViewModel(
            getListOfTasksUseCase = get(),
            saveTaskUseCase = get(),
            deleteTasksUseCase = get(),
            exceptionHandler = get()
        )
    }

    viewModel<EditorViewModel> { (bundle: Bundle) ->
        EditorViewModel(
            arguments = bundle,
            getTaskByIdUseCase = get(),
            saveTaskUseCase = get(),
            deleteTaskUseCase = get(),
            snackbarChannel = get(),
            exceptionHandler = get()
        )
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(
            getPreferencesUseCase = get(),
            savePreferencesUseCase = get(),
            exportTasksUseCase = get(),
            importTasksUseCase = get(),
            snackbarChannel = get(),
            exceptionHandler = get()
        )
    }
}
