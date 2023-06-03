package com.rustamft.tasksft.data.repository

import com.rustamft.tasksft.data.model.map
import com.rustamft.tasksft.data.storage.PreferencesStorage
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.domain.repository.PreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class PreferencesRepositoryImpl(
    private val preferencesStorage: PreferencesStorage,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : PreferencesRepository {

    override suspend fun savePreferences(preferences: Preferences) {
        withContext(dispatcher) {
            preferencesStorage.save(preferences = preferences.map())
        }
    }

    override fun getPreferences(): Flow<Preferences> {
        return preferencesStorage.get().map { preferences ->
            preferences?.map() ?: Preferences()
        }
    }
}
