package com.rustamft.tasksft.domain.repository

import com.rustamft.tasksft.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun saveTask(task: Task)
    suspend fun saveTasks(list: List<Task>)
    suspend fun deleteTask(task: Task)
    suspend fun deleteTasks(list: List<Task>)
    fun getAllTasks(): Flow<List<Task>>
    fun getTaskById(taskId: Int): Flow<Task?>
}
