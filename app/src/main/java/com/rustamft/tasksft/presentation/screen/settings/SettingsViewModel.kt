package com.rustamft.tasksft.presentation.screen.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.AppPreferences
import com.rustamft.tasksft.domain.usecase.ExportTasksUseCase
import com.rustamft.tasksft.domain.usecase.GetAppPreferencesUseCase
import com.rustamft.tasksft.domain.usecase.ImportTasksUseCase
import com.rustamft.tasksft.domain.usecase.SaveAppPreferencesUseCase
import com.rustamft.tasksft.presentation.util.UIText
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class SettingsViewModel(
    getAppPreferencesUseCase: GetAppPreferencesUseCase,
    private val saveAppPreferencesUseCase: SaveAppPreferencesUseCase,
    private val exportTasksUseCase: ExportTasksUseCase,
    private val importTasksUseCase: ImportTasksUseCase,
    private val snackbarChannel: Channel<UIText>,
    private val exceptionHandler: CoroutineExceptionHandler
) : ViewModel() {

    private val successChannel = Channel<Boolean>()
    val successFlow = successChannel.receiveAsFlow()
    val appPreferencesFlow: Flow<AppPreferences> = getAppPreferencesUseCase.execute()

    fun setTheme(darkTheme: Boolean?) {
        viewModelScope.launch(exceptionHandler) {
            supervisorScope {
                saveAppPreferencesUseCase.execute(
                    appPreferences = appPreferencesFlow.first().copy(
                        darkTheme = darkTheme
                    )
                )
            }
        }
    }

    fun exportTasks(directoryUri: Uri?) {
        launchInViewModelScope(
            successMessage = UIText.StringResource(R.string.backup_file_exported)
        ) {
            listOf(
                exportTasksUseCase.execute(directoryUriString = directoryUri.toString()),
                saveAppPreferencesUseCase.execute(
                    appPreferences = appPreferencesFlow.first().copy(
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
            supervisorScope {
                val jobs = blocks.map { block -> launch { block() } }
                jobs.joinAll()
                if (successMessage != null) {
                    snackbarChannel.send(successMessage)
                }
                successChannel.send(true)
            }
        }
    }
}
