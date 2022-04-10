package com.rustamft.tasksft.database.room

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TasksRoomDatabaseModule {

    @Provides
    fun provideTasksRoomDao(roomDatabase: TasksRoomDatabase): TasksRoomDao {
        return roomDatabase.tasksDao()
    }

    @Provides
    @Singleton
    fun provideTasksRoomDatabase(@ApplicationContext context: Context): TasksRoomDatabase {
        return Room.databaseBuilder(
            context,
            TasksRoomDatabase::class.java,
            "app_database"
        ).build()
    }
}
