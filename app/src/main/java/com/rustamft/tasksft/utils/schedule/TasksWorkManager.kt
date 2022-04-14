package com.rustamft.tasksft.utils.schedule

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.work.WorkManager
import com.rustamft.tasksft.database.entity.Task
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

interface TasksWorkManager {

    @Module
    @InstallIn(SingletonComponent::class)
    class TasksWorkManagerModule {

        @Provides
        @Singleton
        fun provideTasksWorkManager(@ApplicationContext context: Context): TasksWorkManager {
            return TasksWorkManagerImpl(
                WorkManager.getInstance(context),
                NotificationManagerCompat.from(context)
            )
        }
    }

    fun scheduleOneTime(task: Task)
    fun cancel(task: Task)
    suspend fun cancel(list: List<Task>)
}
