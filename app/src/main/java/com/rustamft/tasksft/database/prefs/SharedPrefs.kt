package com.rustamft.tasksft.database.prefs

import android.net.Uri
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

interface SharedPrefs {

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class SharedPrefsModule {
        @Binds
        @Singleton
        abstract fun bindSharedPrefs(sharedPrefs: SharedPrefsImpl): SharedPrefs
    }

    fun setNightMode(mode: Int)
    fun getNightMode(): Int
    fun setBackupDir(uri: Uri)
    fun getBackupDir(): String?
    fun hasBackupDirPermission(): Boolean
}
