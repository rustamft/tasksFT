package com.rustamft.tasksft.domain.usecase

import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.domain.repository.PreferencesRepository

class SavePreferencesUseCase(
    private val preferencesRepository: PreferencesRepository
) {

    suspend fun execute(preferences: Preferences) {
        preferencesRepository.savePreferences(preferences = preferences)
    }
}
