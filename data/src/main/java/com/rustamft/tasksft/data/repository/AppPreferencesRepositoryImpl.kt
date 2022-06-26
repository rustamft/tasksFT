package com.rustamft.tasksft.data.repository

import com.rustamft.tasksft.data.model.AppPreferencesData
import com.rustamft.tasksft.data.storage.AppPreferencesStorage
import com.rustamft.tasksft.data.util.convertTo
import com.rustamft.tasksft.domain.model.AppPreferences
import com.rustamft.tasksft.domain.repository.AppPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class AppPreferencesRepositoryImpl(
    private val appPreferencesStorage: AppPreferencesStorage,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : AppPreferencesRepository {

    override suspend fun saveAppPreferences(appPreferences: AppPreferences) {
        withContext(dispatcher) {
            appPreferencesStorage.save(
                appPreferencesData = appPreferences.convertTo(
                    AppPreferencesData::class.java
                )
            )
        }
    }

    override fun getAppPreferences(): Flow<AppPreferences> =
        appPreferencesStorage.get().map { it.convertTo(AppPreferences::class.java) }
}
