package com.rustamft.tasksft.data.repository

import com.rustamft.tasksft.data.model.map
import com.rustamft.tasksft.data.storage.TaskStorage
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.repository.TaskRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException

internal class TaskRepositoryImpl(
    private val taskStorage: TaskStorage,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TaskRepository {

    @Throws(IOException::class, Exception::class)
    override suspend fun save(task: Task) {
        withContext(dispatcher) {
            taskStorage.save(task = task.map())
        }
    }

    @Throws(IOException::class, Exception::class)
    override suspend fun save(tasks: List<Task>) {
        withContext(dispatcher) {
            taskStorage.save(tasks = tasks.map())
        }
    }

    @Throws(IOException::class, Exception::class)
    override suspend fun delete(task: Task) {
        withContext(dispatcher) {
            taskStorage.delete(task = task.map())
        }
    }

    @Throws(IOException::class, Exception::class)
    override suspend fun delete(tasks: List<Task>) {
        withContext(dispatcher) {
            taskStorage.delete(tasks = tasks.map())
        }
    }

    override fun getAll(): Flow<List<Task>> {
        return taskStorage.getAll().map()
    }

    override fun get(taskId: Int): Flow<Task> {
        return taskStorage.get(taskId = taskId).map { task ->
            task?.map() ?: Task()
        }
    }
}
