package com.rustamft.tasksft.domain.usecase

import com.rustamft.tasksft.domain.repository.BackupRepository
import com.rustamft.tasksft.domain.repository.TaskRepository
import com.rustamft.tasksft.domain.service.TaskBackupService
import com.rustamft.tasksft.domain.service.TaskBackupService.OperationType
import kotlinx.coroutines.flow.first

class ExportTasksUseCase(
    private val backupRepository: BackupRepository,
    private val tasksRepository: TaskRepository,
    private val taskBackupService: TaskBackupService
) {

    suspend fun execute(directoryUriString: String) {
        taskBackupService.start(OperationType.EXPORT) {
            backupRepository.saveBackup(
                directoryUriString = directoryUriString,
                list = tasksRepository.getAllTasks().first()
            )
        }
    }
}
