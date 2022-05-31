package com.rustamft.tasksft.domain.usecase

import com.rustamft.tasksft.domain.model.AppPreferences
import com.rustamft.tasksft.domain.repository.AppPreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetAppPreferencesUseCase(
    private val appPreferencesRepository: AppPreferencesRepository
) {

    fun execute(): Flow<AppPreferences> = appPreferencesRepository.getAppPreferences()
}
