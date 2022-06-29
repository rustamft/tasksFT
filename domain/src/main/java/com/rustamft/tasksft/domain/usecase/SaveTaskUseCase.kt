package com.rustamft.tasksft.domain.usecase

import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.notification.TaskWorkManager
import com.rustamft.tasksft.domain.repository.TaskRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.IOException

class SaveTaskUseCase(
    private val taskRepository: TaskRepository,
    private val taskWorkManager: TaskWorkManager
) {

    @Throws(IOException::class, Exception::class)
    suspend fun execute(task: Task) {
        coroutineScope {
            launch {
                taskRepository.saveTask(task = task)
            }
            launch {
                taskWorkManager.cancel(task = task)
                if (!task.isFinished && task.reminder != 0L) {
                    taskWorkManager.scheduleOneTime(task = task)
                }
            }
        }
    }
}
