package com.rustamft.tasksft.domain.repository

import com.rustamft.tasksft.domain.model.Preferences
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    suspend fun savePreferences(preferences: Preferences)
    fun getPreferences(): Flow<Preferences>
}
