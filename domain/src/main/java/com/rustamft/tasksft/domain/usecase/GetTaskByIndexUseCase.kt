package com.rustamft.tasksft.domain.usecase

import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTaskByIndexUseCase(
    private val taskRepository: TaskRepository
) {

    fun execute(taskIndex: Int): Flow<Task> = taskRepository.getAllTasks().map { it[taskIndex] }
}
