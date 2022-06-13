package com.rustamft.tasksft.domain.repository

import com.rustamft.tasksft.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun saveTask(task: Task)
    suspend fun deleteTasks(list: List<Task>)
    fun getAllTasks(): Flow<List<Task>>
}
