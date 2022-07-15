package com.rustamft.tasksft.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.room.Room
import com.rustamft.tasksft.data.model.AppPreferencesData
import com.rustamft.tasksft.data.repository.AppPreferencesRepositoryImpl
import com.rustamft.tasksft.data.repository.BackupRepositoryImpl
import com.rustamft.tasksft.data.repository.TaskRepositoryImpl
import com.rustamft.tasksft.data.storage.AppPreferencesStorage
import com.rustamft.tasksft.data.storage.BackupStorage
import com.rustamft.tasksft.data.storage.TaskStorage
import com.rustamft.tasksft.data.storage.datastore.AppPreferencesDataStore
import com.rustamft.tasksft.data.storage.disk.BackupDiskStorage
import com.rustamft.tasksft.data.storage.room.TaskRoomDatabase
import com.rustamft.tasksft.domain.repository.AppPreferencesRepository
import com.rustamft.tasksft.domain.repository.BackupRepository
import com.rustamft.tasksft.domain.repository.TaskRepository
import com.rustamft.tasksft.domain.util.STORED_PREFERENCES
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val dataModule = module {

    single<DataStore<AppPreferencesData>> {
        val context: Context = get()
        DataStoreFactory.create(
            serializer = AppPreferencesData.Serializer,
            produceFile = { context.dataStoreFile(STORED_PREFERENCES) },
            corruptionHandler = null,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    single<AppPreferencesStorage> {
        AppPreferencesDataStore(dataStore = get())
    }

    single<AppPreferencesRepository> {
        AppPreferencesRepositoryImpl(appPreferencesStorage = get())
    }

    single<TaskRoomDatabase> {
        Room.databaseBuilder(
            get(),
            TaskRoomDatabase::class.java,
            "app_database"
        ).build()
    }

    single<TaskStorage> {
        val taskRoomDatabase: TaskRoomDatabase = get()
        taskRoomDatabase.taskRoomDao()
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
