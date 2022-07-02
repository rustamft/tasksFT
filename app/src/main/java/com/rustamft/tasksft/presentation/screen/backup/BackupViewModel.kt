package com.rustamft.tasksft.presentation.screen.backup

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustamft.tasksft.domain.usecase.ExportTasksUseCase
import com.rustamft.tasksft.domain.usecase.GetAppPreferencesUseCase
import com.rustamft.tasksft.domain.usecase.ImportTasksUseCase
import com.rustamft.tasksft.domain.usecase.SaveAppPreferencesUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BackupViewModel(
    getAppPreferencesUseCase: GetAppPreferencesUseCase,
    private val saveAppPreferencesUseCase: SaveAppPreferencesUseCase,
    private val exportTasksUseCase: ExportTasksUseCase,
    private val importTasksUseCase: ImportTasksUseCase,
    private val snackbarChannel: Channel<String>
) : ViewModel() {

    val appPreferencesFlow = getAppPreferencesUseCase.execute()

    fun exportTasks(directoryUri: Uri?) {
        viewModelScope.launch {
            kotlin.runCatching {
                exportTasksUseCase.execute(directoryUriString = directoryUri.toString())
                saveAppPreferencesUseCase.execute(
                    appPreferences = appPreferencesFlow.first().copy(
                        backupDirectory = directoryUri.toString()
                    )
                )
            }.onFailure {
                snackbarChannel.send(it.message.toString())
            }
        }
    }

    fun importTasks(fileUri: Uri?) {
        viewModelScope.launch {
            kotlin.runCatching {
                importTasksUseCase.execute(fileUriString = fileUri.toString())
            }.onFailure {
                snackbarChannel.send(it.message.toString())
            }
        }
    }
}
