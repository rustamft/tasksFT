package com.rustamft.tasksft.domain.usecase

import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.repository.TaskRepository
import java.io.IOException

class DeleteTasksUseCase(
    private val taskRepository: TaskRepository
) {

    @Throws(IOException::class, Exception::class)
    suspend fun execute(list: List<Task>) = taskRepository.deleteTasks(list = list)
}
