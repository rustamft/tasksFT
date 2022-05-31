package com.rustamft.tasksft.data.storage

import com.rustamft.tasksft.data.model.AppPreferencesData
import kotlinx.coroutines.flow.Flow

internal interface AppPreferencesStorage {

    suspend fun save(appPreferencesData: AppPreferencesData)
    fun get(): Flow<AppPreferencesData>
}
