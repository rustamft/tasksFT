package com.rustamft.tasksft.data.repository

import com.rustamft.tasksft.data.storage.TaskStorage
import com.rustamft.tasksft.data.util.convert
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.repository.TaskRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.IOException

internal class TaskRepositoryImpl(
    private val taskStorage: TaskStorage,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TaskRepository {

    @Throws(IOException::class, Exception::class)
    override suspend fun saveTask(task: Task) {
        withContext(dispatcher) {
            taskStorage.save(taskData = task.convert())
        }
    }

    @Throws(IOException::class, Exception::class)
    override suspend fun deleteTasks(list: List<Task>) {
        withContext(dispatcher) {
            taskStorage.delete(list = list.convert())
        }
    }

    override fun getAllTasks(): Flow<List<Task>> {
        return taskStorage.getAll().convert()
    }
}
