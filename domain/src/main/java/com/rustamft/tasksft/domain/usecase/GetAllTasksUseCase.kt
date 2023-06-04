package com.rustamft.tasksft.domain.usecase

import com.rustamft.tasksft.domain.global.defaultSort
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllTasksUseCase(
    private val taskRepository: TaskRepository
) {

    fun execute(): Flow<List<Task>> = taskRepository.getAll().map { it.defaultSort() }
}
