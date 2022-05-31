package com.rustamft.tasksft.domain.repository

import com.rustamft.tasksft.domain.model.AppPreferences
import kotlinx.coroutines.flow.Flow

interface AppPreferencesRepository {

    suspend fun saveAppPreferences(appPreferences: AppPreferences)
    fun getAppPreferences(): Flow<AppPreferences>
}
