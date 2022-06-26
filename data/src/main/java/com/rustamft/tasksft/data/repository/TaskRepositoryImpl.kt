package com.rustamft.tasksft.data.repository

import com.rustamft.tasksft.data.model.TaskData
import com.rustamft.tasksft.data.storage.TaskStorage
import com.rustamft.tasksft.data.util.convertToListOf
import com.rustamft.tasksft.data.util.convertTo
import com.rustamft.tasksft.data.util.convertToFlowOf
import com.rustamft.tasksft.data.util.convertToFlowOfListOf
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
            taskStorage.save(taskData = task.convertTo(TaskData::class.java))
        }
    }

    @Throws(IOException::class, Exception::class)
    override suspend fun deleteTasks(list: List<Task>) {
        withContext(dispatcher) {
            taskStorage.delete(list = list.convertToListOf(TaskData::class.java))
        }
    }

    override fun getAllTasks(): Flow<List<Task>> {
        return taskStorage.getAll().convertToFlowOfListOf(Task::class.java)
    }

    override fun getTaskById(taskId: Int): Flow<Task> {
        return taskStorage.getById(id = taskId).convertToFlowOf(Task::class.java)
    }
}
