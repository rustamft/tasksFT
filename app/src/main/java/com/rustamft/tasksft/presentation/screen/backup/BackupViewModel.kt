package com.rustamft.tasksft.presentation.screen.backup

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.usecase.ExportTasksUseCase
import com.rustamft.tasksft.domain.usecase.GetAppPreferencesUseCase
import com.rustamft.tasksft.domain.usecase.ImportTasksUseCase
import com.rustamft.tasksft.domain.usecase.SaveAppPreferencesUseCase
import com.rustamft.tasksft.presentation.util.UIText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class BackupViewModel(
    getAppPreferencesUseCase: GetAppPreferencesUseCase,
    private val saveAppPreferencesUseCase: SaveAppPreferencesUseCase,
    private val exportTasksUseCase: ExportTasksUseCase,
    private val importTasksUseCase: ImportTasksUseCase,
    private val snackbarChannel: Channel<UIText>
) : ViewModel() {

    private val successChannel = Channel<Boolean>()
    val successFlow = successChannel.receiveAsFlow()

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
            }.onSuccess {
                snackbarChannel.send(UIText.StringResource(R.string.backup_file_exported))
                successChannel.send(true)
            }.onFailure {
                snackbarChannel.send(UIText.DynamicString(it.message.toString()))
            }
        }
    }

    fun importTasks(fileUri: Uri?) {
        viewModelScope.launch {
            kotlin.runCatching {
                importTasksUseCase.execute(fileUriString = fileUri.toString())
            }.onSuccess {
                snackbarChannel.send(UIText.StringResource(R.string.backup_file_imported))
                successChannel.send(true)
            }.onFailure {
                snackbarChannel.send(UIText.DynamicString(it.message.toString()))
            }
        }
    }
}
