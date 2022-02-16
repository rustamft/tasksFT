package com.rustamft.tasksft.database.repository

import com.rustamft.tasksft.database.entity.AppTask
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

interface AppRepo {

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class RepoModule {
        @Binds
        @Singleton
        abstract fun bindRepo(roomRepo: TasksRoomRepo): AppRepo
    }

    suspend fun save(task: AppTask)
    suspend fun update(task: AppTask)
    suspend fun delete(task: AppTask)
    suspend fun delete(list: List<AppTask>)
    fun getList(): Flow<List<AppTask>>
    suspend fun getIds(): List<Int>
    suspend fun getFinished(): List<AppTask>
    suspend fun getEntity(id: Int): AppTask
}
