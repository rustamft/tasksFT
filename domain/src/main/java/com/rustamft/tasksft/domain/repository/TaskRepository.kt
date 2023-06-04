package com.rustamft.tasksft.domain.repository

import com.rustamft.tasksft.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun save(task: Task)
    suspend fun save(tasks: List<Task>)
    suspend fun delete(task: Task)
    suspend fun delete(tasks: List<Task>)
    fun getAll(): Flow<List<Task>>
    fun get(taskId: Int): Flow<Task?>
}
