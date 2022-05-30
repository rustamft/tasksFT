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

    override suspend fun saveTask(task: Task) {
        withContext(Dispatchers.IO) {
            taskStorage.save(taskData = task.convert())
        }
    }

    override fun getTask(): Flow<Task> = taskStorage.get().map { it.convert() }
}
