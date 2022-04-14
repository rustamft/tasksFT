package com.rustamft.tasksft.database.repository

import com.rustamft.tasksft.database.entity.Task
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds
    @Singleton
    abstract fun bindRepo(roomRepo: TasksRoomRepo): Repo<Task>
}
