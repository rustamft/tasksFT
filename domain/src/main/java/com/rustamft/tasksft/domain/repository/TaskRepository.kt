package com.rustamft.tasksft.domain.repository

import com.rustamft.tasksft.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun saveTask(id: Int, task: Task)
    fun getTaskById(id: Int): Flow<Task>
    fun getAllTasks(): Flow<Map<Int, Task>>
}
