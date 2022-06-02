package com.rustamft.tasksft.domain.usecase

import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.repository.TaskRepository

class SaveTaskUseCase(
    private val taskRepository: TaskRepository
) {

    suspend fun execute(task: Task) = taskRepository.saveTask(task = task)
}
