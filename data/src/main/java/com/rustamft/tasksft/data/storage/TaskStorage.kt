package com.rustamft.tasksft.data.storage

import com.rustamft.tasksft.data.model.TaskData
import kotlinx.coroutines.flow.Flow

internal interface TaskStorage {

    suspend fun save(taskData: TaskData)
    suspend fun delete(list: List<TaskData>)
    fun getAll(): Flow<List<TaskData>>
}
