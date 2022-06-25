package com.rustamft.tasksft.data.storage.datastore

import androidx.datastore.core.DataStore
import com.rustamft.tasksft.data.model.AppPreferencesData
import com.rustamft.tasksft.data.model.container.DataContainer
import com.rustamft.tasksft.data.storage.AppPreferencesStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AppPreferencesDataStore(
    private val dataStore: DataStore<DataContainer>
) : AppPreferencesStorage {

    override suspend fun save(appPreferencesData: AppPreferencesData) {
        dataStore.updateData {
            it.copy(appPreferencesData = appPreferencesData)
        }
    }

    override fun get(): Flow<AppPreferencesData> = dataStore.data.map { it.appPreferencesData }
}
