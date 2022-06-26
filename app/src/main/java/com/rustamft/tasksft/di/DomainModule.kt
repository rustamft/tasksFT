package com.rustamft.tasksft.di

import com.rustamft.tasksft.domain.repository.AppPreferencesRepository
import com.rustamft.tasksft.domain.repository.TaskRepository
import com.rustamft.tasksft.domain.usecase.DeleteTasksUseCase
import com.rustamft.tasksft.domain.usecase.GetAppPreferencesUseCase
import com.rustamft.tasksft.domain.usecase.GetListOfTasksUseCase
import com.rustamft.tasksft.domain.usecase.GetTaskByIdUseCase
import com.rustamft.tasksft.domain.usecase.SaveTaskUseCase
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
    fun provideGetListOfTasksUseCase(taskRepository: TaskRepository): GetListOfTasksUseCase {
        return GetListOfTasksUseCase(taskRepository = taskRepository)
    }

    @Provides
    fun provideGetTaskByIdUseCase(taskRepository: TaskRepository): GetTaskByIdUseCase {
        return GetTaskByIdUseCase(taskRepository = taskRepository)
    }

    @Provides
    fun provideSaveTaskUseCase(taskRepository: TaskRepository): SaveTaskUseCase {
        return SaveTaskUseCase(taskRepository = taskRepository)
    }

    @Provides
    fun provideDeleteTasksUseCase(taskRepository: TaskRepository): DeleteTasksUseCase {
        return DeleteTasksUseCase(taskRepository = taskRepository)
    }
}
