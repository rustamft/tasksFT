package com.rustamft.tasksft.database.repository

import android.content.Context
import com.rustamft.tasksft.database.entity.AppTask
import com.rustamft.tasksft.database.room.TasksRoomDao
import com.rustamft.tasksft.database.room.TasksRoomDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TasksRoomRepo @Inject constructor(
    @ApplicationContext private val context: Context
) : AppRepo {

    private val dao: TasksRoomDao = TasksRoomDatabase.getDatabase(context).tasksDao()


    override suspend fun save(task: AppTask) {
        dao.insert(task)
    }

    override suspend fun update(task: AppTask) {
        dao.update(task)
    }

    override suspend fun delete(task: AppTask) {
        dao.delete(task)
    }

    override suspend fun delete(list: List<AppTask>) {
        dao.delete(list)
    }

    override fun getList(): Flow<List<AppTask>> {
        return dao.getAll()
    }

    override suspend fun getNonExistingId(): Int {
        val ids = dao.getIds()
        var i = 0
        while (ids.indexOf(i) != -1) {
            i++
        }
        return i
    }

    override suspend fun getFinished(): List<AppTask> {
        return dao.getFinished()
    }

    override suspend fun getEntity(id: Int): AppTask {
        return dao.getEntity(id)
    }
}
