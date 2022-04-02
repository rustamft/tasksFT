package com.rustamft.tasksft.database.repository

import android.content.Context
import com.rustamft.tasksft.database.entity.Task
import com.rustamft.tasksft.database.room.TasksRoomDao
import com.rustamft.tasksft.database.room.TasksRoomDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TasksRoomRepo @Inject constructor(
    @ApplicationContext private val context: Context
) : TasksRepo {

    // TODO: move to constructor
    private val dao: TasksRoomDao = TasksRoomDatabase.getDatabase(context).tasksDao()

    override suspend fun insert(task: Task) = dao.insert(task)
    override suspend fun insert(list: List<Task>) = dao.insert(list)
    override suspend fun update(task: Task) = dao.update(task)
    override suspend fun delete(task: Task) = dao.delete(task)
    override suspend fun delete(list: List<Task>) = dao.delete(list)
    override suspend fun getTask(id: Int): Task = dao.getTask(id)
    override fun getTasksList(): Flow<List<Task>> = dao.getTasksList()
    override suspend fun getFinishedTasks(): List<Task> = dao.getFinishedTasks()
}
