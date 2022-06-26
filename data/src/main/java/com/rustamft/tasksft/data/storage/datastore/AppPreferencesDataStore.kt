package com.rustamft.tasksft.data.storage.datastore

import androidx.datastore.core.DataStore
import com.rustamft.tasksft.data.model.AppPreferencesData
import com.rustamft.tasksft.data.storage.AppPreferencesStorage
import kotlinx.coroutines.flow.Flow

internal class AppPreferencesDataStore(
    private val dataStore: DataStore<AppPreferencesData>
) : AppPreferencesStorage {

    override suspend fun save(appPreferencesData: AppPreferencesData) {
        dataStore.updateData { appPreferencesData }
    }

    override fun get(): Flow<AppPreferencesData> = dataStore.data
}
