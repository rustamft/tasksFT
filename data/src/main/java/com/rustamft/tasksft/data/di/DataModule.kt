package com.rustamft.tasksft.data.di

import com.rustamft.tasksft.data.repository.BackupRepositoryImpl
import com.rustamft.tasksft.data.repository.PreferencesRepositoryImpl
import com.rustamft.tasksft.data.repository.TaskRepositoryImpl
import com.rustamft.tasksft.data.storage.BackupStorage
import com.rustamft.tasksft.data.storage.PreferencesStorage
import com.rustamft.tasksft.data.storage.TaskStorage
import com.rustamft.tasksft.data.storage.disk.BackupDiskStorage
import com.rustamft.tasksft.data.storage.room.AppRoomDatabase
import com.rustamft.tasksft.domain.repository.BackupRepository
import com.rustamft.tasksft.domain.repository.PreferencesRepository
import com.rustamft.tasksft.domain.repository.TaskRepository
import org.koin.dsl.module

val dataModule = module {

    single<AppRoomDatabase> {
        AppRoomDatabase.RoomDatabaseCreator.create(context = get())
    }

    single<PreferencesStorage> {
        val appRoomDatabase: AppRoomDatabase = get()
        appRoomDatabase.dao()
    }

    single<PreferencesRepository> {
        PreferencesRepositoryImpl(preferencesStorage = get())
    }

    single<TaskStorage> {
        val appRoomDatabase: AppRoomDatabase = get()
        appRoomDatabase.dao()
    }

    single<TaskRepository> {
        TaskRepositoryImpl(taskStorage = get())
    }

    single<BackupStorage> {
        BackupDiskStorage(context = get())
    }

    single<BackupRepository> {
        BackupRepositoryImpl(backupStorage = get())
    }
}
