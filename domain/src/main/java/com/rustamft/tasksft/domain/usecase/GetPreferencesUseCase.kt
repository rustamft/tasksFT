package com.rustamft.tasksft.domain.usecase

import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetPreferencesUseCase(
    private val preferencesRepository: PreferencesRepository
) {

    fun execute(): Flow<Preferences> = preferencesRepository.getPreferences()
}
