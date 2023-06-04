package com.rustamft.tasksft.domain.usecase

import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.notification.TaskNotificationScheduler
import com.rustamft.tasksft.domain.repository.TaskRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.io.IOException

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository,
    private val taskNotificationScheduler: TaskNotificationScheduler
) {

    @Throws(IOException::class, Exception::class)
    suspend fun execute(task: Task) {
        supervisorScope {
            launch {
                taskRepository.delete(task = task)
            }
            launch {
                taskNotificationScheduler.cancel(task = task)
            }
        }
    }

    @Throws(IOException::class, Exception::class)
    suspend fun execute(tasks: List<Task>) {
        supervisorScope {
            launch {
                taskRepository.delete(tasks = tasks)
            }
            launch {
                taskNotificationScheduler.cancel(list = tasks)
            }
        }
    }
}
