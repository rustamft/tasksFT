package com.rustamft.tasksft.database.repository

import com.rustamft.tasksft.database.entity.AppTask
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class TasksRoomRepoMock : AppRepo {

    val listTasks = mutableListOf(
        AppTask(0, "1", "111", 0L, false)
    )

    override suspend fun getIds(): List<Int> {
        val listIDs = mutableListOf<Int>()
        for (task in listTasks) {
            listIDs.add(task.id)
        }
        return listIDs
    }

    override suspend fun getEntity(id: Int) = listTasks[id]

    override suspend fun save(task: AppTask) {
        delay(1000)
        listTasks.add(task.id, task)
    }

    override suspend fun update(task: AppTask) {
        delay(1000)
        listTasks.removeAt(task.id)
        listTasks.add(task.id, task)
    }

    override suspend fun delete(task: AppTask) {
        //
    }

    override suspend fun delete(list: List<AppTask>) {
        //
    }

    override fun getList(): Flow<List<AppTask>> {
        return emptyFlow()
    }

    override suspend fun getFinished(): List<AppTask> {
        return emptyList()
    }
}