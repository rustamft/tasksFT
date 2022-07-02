package com.rustamft.tasksft.domain.usecase

import com.rustamft.tasksft.domain.repository.BackupRepository
import com.rustamft.tasksft.domain.repository.TaskRepository
import kotlinx.coroutines.flow.first

class ExportTasksUseCase(
    private val backupRepository: BackupRepository,
    private val tasksRepository: TaskRepository
) {

    suspend fun execute(directoryUriString: String) {
        val list = tasksRepository.getAllTasks().first()
        backupRepository.saveBackup(
            directoryUriString = directoryUriString,
            list = list
        )
    }
}
