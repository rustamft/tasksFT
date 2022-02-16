package com.rustamft.tasksft.utils.schedule

import com.rustamft.tasksft.database.entity.AppTask
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

interface AppWorkManager {

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class WorkManagerModule {
        @Binds
        @Singleton
        abstract fun bindWorkManager(workManager: TaskWorkManager): AppWorkManager
    }

    fun scheduleOneTime(task: AppTask)
    fun cancel(task: AppTask)
    fun cancel(list: List<AppTask>)
}
