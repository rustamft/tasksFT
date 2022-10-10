package com.rustamft.tasksft.data.repository

import com.rustamft.tasksft.data.storage.TaskStorage
import com.rustamft.tasksft.data.util.mapFromData
import com.rustamft.tasksft.data.util.mapToData
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
    override suspend fun saveTask(task: Task) {
        withContext(dispatcher) {
            taskStorage.save(taskData = task.mapToData())
        }
    }

    @Throws(IOException::class, Exception::class)
    override suspend fun saveTasks(list: List<Task>) {
        withContext(dispatcher) {
            taskStorage.save(list = list.mapToData())
        }
    }

    @Throws(IOException::class, Exception::class)
    override suspend fun deleteTask(task: Task) {
        withContext(dispatcher) {
            taskStorage.delete(task = task.mapToData())
        }
    }


    @Throws(IOException::class, Exception::class)
    override suspend fun deleteTasks(list: List<Task>) {
        withContext(dispatcher) {
            taskStorage.delete(list = list.mapToData())
        }
    }

    override fun getAllTasks(): Flow<List<Task>> {
        return taskStorage.getAll().mapFromData()
    }

    override fun getTaskById(taskId: Int): Flow<Task> {
        return taskStorage.getById(id = taskId).map { taskData ->
            taskData?.mapFromData() ?: Task()
        }
    }
}
