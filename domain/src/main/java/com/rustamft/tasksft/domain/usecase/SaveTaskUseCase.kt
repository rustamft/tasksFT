package com.rustamft.tasksft.domain.usecase

import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.notification.TaskWorkManager
import com.rustamft.tasksft.domain.repository.TaskRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class SaveTaskUseCase(
    private val taskRepository: TaskRepository,
    private val taskWorkManager: TaskWorkManager
) {

    @Throws(Exception::class)
    suspend fun execute(task: Task) {
        coroutineScope {
            launch {
                taskRepository.saveTask(task = task)
            }
            launch {
                taskWorkManager.cancel(task = task)
                if (!task.finished && task.reminder != 0L) {
                    taskWorkManager.scheduleOneTime(task = task)
                }
            }
        }
    }
}
