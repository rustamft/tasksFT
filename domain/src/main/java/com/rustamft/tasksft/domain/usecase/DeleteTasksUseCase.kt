package com.rustamft.tasksft.domain.usecase

import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.repository.TaskRepository

class DeleteTasksUseCase(
    private val taskRepository: TaskRepository
) {

    suspend fun execute(list: List<Task>) = taskRepository.deleteTasks(list = list)
}
