package com.rustamft.tasksft.di

import android.app.Application
import androidx.core.app.NotificationManagerCompat
import androidx.work.WorkManager
import com.rustamft.tasksft.domain.notification.TaskWorkManager
import com.rustamft.tasksft.domain.usecase.DeleteTasksUseCase
import com.rustamft.tasksft.domain.usecase.GetAppPreferencesUseCase
import com.rustamft.tasksft.domain.usecase.GetListOfTasksUseCase
import com.rustamft.tasksft.domain.usecase.GetTaskByIdUseCase
import com.rustamft.tasksft.domain.usecase.SaveTaskUseCase
import com.rustamft.tasksft.presentation.notification.manager.TaskWorkManagerImpl
import org.koin.dsl.module

val domainModule = module {

    single<TaskWorkManager> {
        val context: Application = get()
        TaskWorkManagerImpl(
            WorkManager.getInstance(context),
            NotificationManagerCompat.from(context)
        )
    }

    factory<GetAppPreferencesUseCase> {
        GetAppPreferencesUseCase(appPreferencesRepository = get())
    }

    factory<GetListOfTasksUseCase> {
        GetListOfTasksUseCase(taskRepository = get())
    }

    factory<GetTaskByIdUseCase> {
        GetTaskByIdUseCase(taskRepository = get())
    }

    factory<SaveTaskUseCase> {
        SaveTaskUseCase(taskRepository = get(), taskWorkManager = get())
    }

    factory<DeleteTasksUseCase> {
        DeleteTasksUseCase(taskRepository = get())
    }
}
/* TODO: remove
@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    @Singleton
    fun provideTasksWorkManager(@ApplicationContext context: Context): TaskWorkManager {
        return TaskWorkManagerImpl(
            WorkManager.getInstance(context),
            NotificationManagerCompat.from(context)
        )
    }

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
    fun provideSaveTaskUseCase(
        taskRepository: TaskRepository,
        taskWorkManager: TaskWorkManager
    ): SaveTaskUseCase {
        return SaveTaskUseCase(
            taskRepository = taskRepository,
            taskWorkManager = taskWorkManager
        )
    }

    @Provides
    fun provideDeleteTasksUseCase(taskRepository: TaskRepository): DeleteTasksUseCase {
        return DeleteTasksUseCase(taskRepository = taskRepository)
    }
}
*/
