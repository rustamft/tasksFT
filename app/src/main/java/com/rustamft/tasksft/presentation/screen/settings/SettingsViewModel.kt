package com.rustamft.tasksft.presentation.screen.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Preferences.Theme
import com.rustamft.tasksft.domain.usecase.ExportTasksUseCase
import com.rustamft.tasksft.domain.usecase.GetPreferencesUseCase
import com.rustamft.tasksft.domain.usecase.ImportTasksUseCase
import com.rustamft.tasksft.domain.usecase.SavePreferencesUseCase
import com.rustamft.tasksft.presentation.util.UIText
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class SettingsViewModel(
    getPreferencesUseCase: GetPreferencesUseCase,
    private val savePreferencesUseCase: SavePreferencesUseCase,
    private val exportTasksUseCase: ExportTasksUseCase,
    private val importTasksUseCase: ImportTasksUseCase,
    private val snackbarChannel: Channel<UIText>,
    private val exceptionHandler: CoroutineExceptionHandler
) : ViewModel() {

    private val successChannel = Channel<Boolean>()
    val successFlow = successChannel.receiveAsFlow()
    val preferencesFlow = getPreferencesUseCase.execute()

    fun setTheme(theme: Theme) {
        viewModelScope.launch(exceptionHandler) {
            savePreferencesUseCase.execute(
                preferences = preferencesFlow.first().copy(theme = theme)
            )
        }
    }

    fun exportTasks(directoryUri: Uri?) {
        launchInViewModelScope(
            successMessage = UIText.StringResource(R.string.backup_file_exported)
        ) {
            listOf(
                exportTasksUseCase.execute(directoryUriString = directoryUri.toString()),
                savePreferencesUseCase.execute(
                    preferences = preferencesFlow.first().copy(
                        backupDirectory = directoryUri.toString()
                    )
                )
            )
        }
    }

    fun importTasks(fileUri: Uri?) {
        launchInViewModelScope(
            successMessage = UIText.StringResource(R.string.backup_file_imported)
        ) {
            importTasksUseCase.execute(fileUriString = fileUri.toString())
        }
    }

    private fun launchInViewModelScope(
        successMessage: UIText? = null,
        block: suspend CoroutineScope.() -> Unit
    ) {
        launchInViewModelScope(successMessage, listOf(block))
    }

    private fun launchInViewModelScope(
        successMessage: UIText? = null,
        blocks: List<suspend CoroutineScope.() -> Unit>
    ) {
        viewModelScope.launch(exceptionHandler) {
            val jobs = blocks.map { block -> launch { block() } }
            jobs.joinAll()
            if (successMessage != null) {
                snackbarChannel.send(successMessage)
            }
            successChannel.send(true)
        }
    }
}
