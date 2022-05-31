package com.rustamft.tasksft.data.repository

import com.rustamft.tasksft.data.storage.TaskStorage
import com.rustamft.tasksft.data.util.convert
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class TaskRepositoryImpl(
    private val taskStorage: TaskStorage
) : TaskRepository {

    override suspend fun saveTask(id: Int, task: Task) {
        withContext(Dispatchers.IO) {
            taskStorage.save(id = id, taskData = task.convert())
        }
    }

    override fun getTaskById(id: Int): Flow<Task> {
        return taskStorage.getById(id = id).map { it.convert() }
    }

    override fun getAllTasks(): Flow<Map<Int, Task>> {
        return taskStorage.getAll().map {
            val map: Map<Int, Task> = it.convert()
            map
        }
    }
}
