package com.rustamft.tasksft.data.storage

import com.rustamft.tasksft.data.model.PreferencesData
import kotlinx.coroutines.flow.Flow

internal interface PreferencesStorage {

    suspend fun save(preferences: PreferencesData)
    fun get(): Flow<PreferencesData?>
}
