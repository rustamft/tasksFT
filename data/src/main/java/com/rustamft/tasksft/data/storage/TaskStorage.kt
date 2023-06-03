package com.rustamft.tasksft.data.storage

import com.rustamft.tasksft.data.model.TaskData
import kotlinx.coroutines.flow.Flow

internal interface TaskStorage {

    suspend fun save(task: TaskData)
    suspend fun save(tasks: List<TaskData>)
    suspend fun delete(task: TaskData)
    suspend fun delete(tasks: List<TaskData>)
    fun getAll(): Flow<List<TaskData>>
    fun get(taskId: Int): Flow<TaskData?>
    fun getFinished(): Flow<List<TaskData>>
}
