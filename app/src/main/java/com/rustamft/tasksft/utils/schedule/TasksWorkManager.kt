package com.rustamft.tasksft.utils.schedule

import com.rustamft.tasksft.database.entity.Task
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

interface TasksWorkManager {

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class TasksWorkManagerModule {
        @Binds
        @Singleton
        abstract fun bindTasksWorkManager(workManager: TasksWorkManagerImpl): TasksWorkManager
    }

    fun scheduleOneTime(task: Task)
    fun cancel(task: Task)
    suspend fun cancel(list: List<Task>)
}
