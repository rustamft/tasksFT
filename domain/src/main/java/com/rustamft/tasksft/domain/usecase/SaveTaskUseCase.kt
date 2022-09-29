package com.rustamft.tasksft.domain.usecase

import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.notification.TaskNotificationScheduler
import com.rustamft.tasksft.domain.repository.TaskRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class SaveTaskUseCase(
    private val taskRepository: TaskRepository,
    private val taskNotificationScheduler: TaskNotificationScheduler
) {

    @Throws(Exception::class)
    suspend fun execute(task: Task) {
        coroutineScope {
            launch {
                taskRepository.saveTask(task = task)
            }
            launch {
                taskNotificationScheduler.cancel(task = task)
                if (!task.finished && task.reminder != 0L) {
                    taskNotificationScheduler.scheduleOneTime(task = task)
                }
            }
        }
    }
}
