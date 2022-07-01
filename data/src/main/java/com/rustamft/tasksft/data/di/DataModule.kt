package com.rustamft.tasksft.data.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.room.Room
import com.rustamft.tasksft.data.model.AppPreferencesData
import com.rustamft.tasksft.data.repository.AppPreferencesRepositoryImpl
import com.rustamft.tasksft.data.repository.TaskRepositoryImpl
import com.rustamft.tasksft.data.storage.AppPreferencesStorage
import com.rustamft.tasksft.data.storage.TaskStorage
import com.rustamft.tasksft.data.storage.datastore.AppPreferencesDataStore
import com.rustamft.tasksft.data.storage.room.TaskRoomDatabase
import com.rustamft.tasksft.domain.repository.AppPreferencesRepository
import com.rustamft.tasksft.domain.repository.TaskRepository
import com.rustamft.tasksft.domain.util.STORED_PREFERENCES
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val dataModule = module {

    single<DataStore<AppPreferencesData>> {
        val context: Application = get()
        DataStoreFactory.create(
            serializer = AppPreferencesData.Serializer,
            produceFile = { context.dataStoreFile(STORED_PREFERENCES) },
            corruptionHandler = null,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    single<TaskRoomDatabase> {
        val context: Application = get()
        Room.databaseBuilder(
            context,
            TaskRoomDatabase::class.java,
            "app_database"
        ).build()
    }

    single<TaskStorage> {
        val taskRoomDatabase: TaskRoomDatabase = get()
        taskRoomDatabase.tasksDao()
    }

    single<TaskRepository> {
        TaskRepositoryImpl(taskStorage = get())
    }

    single<AppPreferencesStorage> {
        AppPreferencesDataStore(dataStore = get())
    }

    single<AppPreferencesRepository> {
        AppPreferencesRepositoryImpl(appPreferencesStorage = get())
    }
}
