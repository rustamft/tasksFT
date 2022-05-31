package com.rustamft.tasksft.data.storage

import com.rustamft.tasksft.data.model.TaskData
import kotlinx.coroutines.flow.Flow

internal interface TaskStorage {

    suspend fun save(id: Int, taskData: TaskData)
    fun getById(id: Int): Flow<TaskData>
    fun getAll(): Flow<Map<Int, TaskData>>
}
