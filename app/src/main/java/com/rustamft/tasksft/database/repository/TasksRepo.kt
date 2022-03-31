package com.rustamft.tasksft.database.repository

import com.rustamft.tasksft.database.entity.Task
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

interface TasksRepo {

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class RepoModule {
        @Binds
        @Singleton
        abstract fun bindRepo(roomRepo: TasksRoomRepo): TasksRepo
    }

    suspend fun save(task: Task)
    suspend fun update(task: Task)
    suspend fun delete(task: Task)
    suspend fun delete(list: List<Task>)
    fun getTasksList(): Flow<List<Task>>
    suspend fun getFinishedTasks(): List<Task>
    suspend fun getTask(id: Int): Task
}
