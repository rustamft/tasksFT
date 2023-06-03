package com.rustamft.tasksft.domain.usecase

import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetTaskByIdUseCase(
    private val taskRepository: TaskRepository
) {

    fun execute(taskId: Int): Flow<Task> = taskRepository.get(taskId = taskId)
}
