package com.rustamft.tasksft.di

import androidx.core.app.NotificationManagerCompat
import androidx.work.WorkManager
import com.rustamft.tasksft.domain.notification.TaskNotificationScheduler
import com.rustamft.tasksft.domain.usecase.DeleteTaskUseCase
import com.rustamft.tasksft.domain.usecase.ExportTasksUseCase
import com.rustamft.tasksft.domain.usecase.GetListOfTasksUseCase
import com.rustamft.tasksft.domain.usecase.GetPreferencesUseCase
import com.rustamft.tasksft.domain.usecase.GetTaskByIdUseCase
import com.rustamft.tasksft.domain.usecase.ImportTasksUseCase
import com.rustamft.tasksft.domain.usecase.SavePreferencesUseCase
import com.rustamft.tasksft.domain.usecase.SaveTaskUseCase
import com.rustamft.tasksft.presentation.notification.manager.TaskNotificationSchedulerImpl
import org.koin.dsl.module

val domainModule = module {

    single<TaskNotificationScheduler> {
        TaskNotificationSchedulerImpl(
            WorkManager.getInstance(get()),
            NotificationManagerCompat.from(get())
        )
    }

    single<GetPreferencesUseCase> {
        GetPreferencesUseCase(preferencesRepository = get())
    }

    factory<GetListOfTasksUseCase> {
        GetListOfTasksUseCase(taskRepository = get())
    }

    factory<GetTaskByIdUseCase> {
        GetTaskByIdUseCase(taskRepository = get())
    }

    factory<SavePreferencesUseCase> {
        SavePreferencesUseCase(preferencesRepository = get())
    }

    factory<SaveTaskUseCase> {
        SaveTaskUseCase(taskRepository = get(), taskNotificationScheduler = get())
    }

    factory<DeleteTaskUseCase> {
        DeleteTaskUseCase(taskRepository = get(), taskNotificationScheduler = get())
    }

    factory<ExportTasksUseCase> {
        ExportTasksUseCase(
            backupRepository = get(),
            tasksRepository = get()
        )
    }

    factory<ImportTasksUseCase> {
        ImportTasksUseCase(
            backupRepository = get(),
            tasksRepository = get(),
            taskNotificationScheduler = get()
        )
    }
}
