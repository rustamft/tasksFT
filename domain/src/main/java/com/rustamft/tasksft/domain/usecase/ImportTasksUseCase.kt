package com.rustamft.tasksft.domain.usecase

import com.rustamft.tasksft.domain.notification.TaskWorkManager
import com.rustamft.tasksft.domain.repository.BackupRepository
import com.rustamft.tasksft.domain.repository.TaskRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ImportTasksUseCase(
    private val backupRepository: BackupRepository,
    private val tasksRepository: TaskRepository,
    private val taskWorkManager: TaskWorkManager
) {

    suspend fun execute(fileUriString: String) {
        val list = backupRepository.getBackup(fileUriString = fileUriString).first()
        coroutineScope {
            launch {
                tasksRepository.saveTasks(list = list)
            }
            launch {
                taskWorkManager.scheduleOneTime(
                    list = list.filter { !it.finished && it.reminder != 0L }
                )
            }
        }
    }
}
