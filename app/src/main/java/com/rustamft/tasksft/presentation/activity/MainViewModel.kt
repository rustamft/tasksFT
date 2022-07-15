package com.rustamft.tasksft.presentation.activity

import androidx.lifecycle.ViewModel
import com.rustamft.tasksft.domain.usecase.GetAppPreferencesUseCase

class MainViewModel(
    getAppPreferencesUseCase: GetAppPreferencesUseCase
) : ViewModel() {

    val appPreferencesFlow = getAppPreferencesUseCase.execute()
}
