package com.rustamft.tasksft.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.rustamft.tasksft.data.model.DataContainer
import com.rustamft.tasksft.data.repository.TaskRepositoryImpl
import com.rustamft.tasksft.data.storage.TaskStorage
import com.rustamft.tasksft.data.storage.datastore.DataStoreTaskStorage
import com.rustamft.tasksft.domain.repository.TaskRepository
import com.rustamft.tasksft.domain.util.STORED_PREFERENCES
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class DataModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<DataContainer> {
        return DataStoreFactory.create(
            serializer = DataContainer.Serializer,
            produceFile = { context.dataStoreFile(STORED_PREFERENCES) },
            corruptionHandler = null,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    @Provides
    @Singleton
    fun provideTaskStorage(dataStore: DataStore<DataContainer>): TaskStorage {
        return DataStoreTaskStorage(dataStore = dataStore)
    }

    @Provides
    @Singleton
    fun provideTaskRepository(taskStorage: TaskStorage): TaskRepository {
        return TaskRepositoryImpl(taskStorage = taskStorage)
    }
}
