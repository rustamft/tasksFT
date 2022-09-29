package com.rustamft.tasksft.data.repository

import com.rustamft.tasksft.data.model.TaskData
import com.rustamft.tasksft.data.storage.TaskStorage
import com.rustamft.tasksft.data.util.mapToListOf
import com.rustamft.tasksft.data.util.mapTo
import com.rustamft.tasksft.data.util.mapToFlowOf
import com.rustamft.tasksft.data.util.mapToFlowOfListOf
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
            taskStorage.save(taskData = task.mapTo(TaskData::class.java))
        }
    }

    @Throws(IOException::class, Exception::class)
    override suspend fun saveTasks(list: List<Task>) {
        withContext(dispatcher) {
            taskStorage.save(list = list.mapToListOf(TaskData::class.java))
        }
    }

    @Throws(IOException::class, Exception::class)
    override suspend fun deleteTask(task: Task) {
        withContext(dispatcher) {
            taskStorage.delete(task = task.mapTo(TaskData::class.java))
        }
    }


    @Throws(IOException::class, Exception::class)
    override suspend fun deleteTasks(list: List<Task>) {
        withContext(dispatcher) {
            taskStorage.delete(list = list.mapToListOf(TaskData::class.java))
        }
    }

    override fun getAllTasks(): Flow<List<Task>> {
        return taskStorage.getAll().mapToFlowOfListOf(Task::class.java)
    }

    override fun getTaskById(taskId: Int): Flow<Task?> {
        return taskStorage.getById(id = taskId).mapToFlowOf(Task::class.java)
    }
}
