package com.rustamft.tasksft.database.prefs

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

interface AppSharedPrefs {

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class SharedPrefsModule {
        @Binds
        @Singleton
        abstract fun bindSharedPrefs(sharedPrefs: TasksSharedPrefs): AppSharedPrefs
    }

    fun setNightMode(mode: Int)
    fun getNightMode(): Int
}
