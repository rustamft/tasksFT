package com.rustamft.tasksft.data.repository

import com.rustamft.tasksft.data.storage.TaskStorage
import com.rustamft.tasksft.data.util.convert
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException

internal class TaskRepositoryImpl(
    private val taskStorage: TaskStorage
) : TaskRepository {

    @Throws(IOException::class, Exception::class)
    override suspend fun saveTask(task: Task) {
        withContext(Dispatchers.IO) {
            taskStorage.save(taskData = task.convert())
        }
    }

    @Throws(IOException::class, Exception::class)
    override suspend fun deleteTasks(list: List<Task>) {
        withContext(Dispatchers.IO) {
            val listData = list.map { task ->
                async { task.convert() }
            }.awaitAll()
            taskStorage.delete(list = listData)
        }
    }

    override fun getAllTasks(): Flow<List<Task>> {
        val listDataFlow = taskStorage.getAll()
        val listFlow = listDataFlow.map { listData ->
            coroutineScope {
                listData.map { taskData ->
                    async {
                        taskData.convert()
                    }
                }.awaitAll()
            }
        }
        return listFlow
    }
}
