package com.rustamft.tasksft.domain.usecase

import com.rustamft.tasksft.domain.notification.TaskNotificationScheduler
import com.rustamft.tasksft.domain.repository.BackupRepository
import com.rustamft.tasksft.domain.repository.TaskRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class ImportTasksUseCase(
    private val backupRepository: BackupRepository,
    private val tasksRepository: TaskRepository,
    private val taskNotificationScheduler: TaskNotificationScheduler
) {

    suspend fun execute(fileUriString: String) {
        val list = backupRepository.get(fileUriString = fileUriString).first()
        supervisorScope {
            launch {
                tasksRepository.save(tasks = list)
            }
            launch {
                taskNotificationScheduler.schedule(
                    list = list.filter { !it.finished && it.reminder != 0L }
                )
            }
        }
    }
}
