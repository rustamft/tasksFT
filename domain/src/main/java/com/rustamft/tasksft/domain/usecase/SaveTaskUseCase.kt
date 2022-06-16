package com.rustamft.tasksft.domain.usecase

import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.repository.TaskRepository
import java.io.IOException

class SaveTaskUseCase(
    private val taskRepository: TaskRepository
) {

    @Throws(IOException::class, Exception::class)
    suspend fun execute(task: Task) = taskRepository.saveTask(task = task)
}
