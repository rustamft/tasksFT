package com.rustamft.tasksft.data.repository

import com.rustamft.tasksft.data.storage.AppPreferencesStorage
import com.rustamft.tasksft.data.util.convert
import com.rustamft.tasksft.domain.model.AppPreferences
import com.rustamft.tasksft.domain.repository.AppPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class AppPreferencesRepositoryImpl(
    private val appPreferencesStorage: AppPreferencesStorage
) : AppPreferencesRepository {

    override suspend fun saveAppPreferences(appPreferences: AppPreferences) {
        withContext(Dispatchers.IO) {
            appPreferencesStorage.save(appPreferencesData = appPreferences.convert())
        }
    }

    override fun getAppPreferences(): Flow<AppPreferences> =
        appPreferencesStorage.get().map { it.convert() }
}
