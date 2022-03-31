package com.rustamft.tasksft.database.repository

import com.rustamft.tasksft.database.entity.Task
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class TasksRoomRepoMock : TasksRepo {

    private val listTasks = mutableListOf<Task>()

    override suspend fun getTask(id: Int) = listTasks[id]

    override suspend fun save(task: Task) {
        delay(1000)
        listTasks.add(task.id, task)
    }

    override suspend fun update(task: Task) {
        delay(1000)
        listTasks.removeAt(task.id)
        listTasks.add(task.id, task)
    }

    override suspend fun delete(task: Task) {
        //
    }

    override suspend fun delete(list: List<Task>) {
        //
    }

    override fun getTasksList(): Flow<List<Task>> {
        return emptyFlow()
    }

    override suspend fun getFinishedTasks(): List<Task> {
        return emptyList()
    }
}
