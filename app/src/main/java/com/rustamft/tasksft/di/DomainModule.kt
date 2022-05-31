package com.rustamft.tasksft.di

import com.rustamft.tasksft.domain.repository.AppPreferencesRepository
import com.rustamft.tasksft.domain.repository.TaskRepository
import com.rustamft.tasksft.domain.usecase.GetAppPreferencesUseCase
import com.rustamft.tasksft.domain.usecase.GetMapOfTasksUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideGetAppPreferencesUseCase(
        appPreferencesRepository: AppPreferencesRepository
    ): GetAppPreferencesUseCase {
        return GetAppPreferencesUseCase(appPreferencesRepository = appPreferencesRepository)
    }

    @Provides
    fun provideGetMapOfTasksUseCase(taskRepository: TaskRepository): GetMapOfTasksUseCase {
        return GetMapOfTasksUseCase(taskRepository = taskRepository)
    }
}
