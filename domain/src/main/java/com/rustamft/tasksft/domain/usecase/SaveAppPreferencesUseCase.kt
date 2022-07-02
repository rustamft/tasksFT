package com.rustamft.tasksft.domain.usecase

import com.rustamft.tasksft.domain.model.AppPreferences
import com.rustamft.tasksft.domain.repository.AppPreferencesRepository

class SaveAppPreferencesUseCase(
    private val appPreferencesRepository: AppPreferencesRepository
) {

    suspend fun execute(appPreferences: AppPreferences) {
        appPreferencesRepository.saveAppPreferences(appPreferences = appPreferences)
    }
}
