package com.rustamft.tasksft.presentation.notification.manager

import androidx.core.app.NotificationManagerCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.notification.TaskNotificationScheduler
import com.rustamft.tasksft.presentation.util.TASK_DESCRIPTION
import com.rustamft.tasksft.presentation.util.TASK_ID
import com.rustamft.tasksft.presentation.util.TASK_TITLE
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.util.concurrent.TimeUnit

class TaskNotificationSchedulerImpl(
    private val workManager: WorkManager,
    private val notificationManager: NotificationManagerCompat
) : TaskNotificationScheduler {

    override fun schedule(task: Task) {
        val now = System.currentTimeMillis()
        var delay: Long = task.reminder.minus(now)
        while (delay < 0 && task.repeat > 0) {
            delay += task.repeat
        }
        val data = workDataOf(
            Pair(TASK_ID, task.id),
            Pair(TASK_TITLE, task.title),
            Pair(TASK_DESCRIPTION, task.description)
        )
        workManager.enqueue(
            if (task.repeat > 0) {
                PeriodicWorkRequestBuilder<PeriodicWorker>(task.repeat, TimeUnit.MILLISECONDS)
            } else {
                OneTimeWorkRequestBuilder<OneTimeWorker>()
            }
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .addTag(task.id.toString())
                .build()
        )
    }

    override suspend fun schedule(list: List<Task>) {
        supervisorScope {
            for (task in list) {
                launch { schedule(task) }
            }
        }
    }

    override fun cancel(task: Task) {
        notificationManager.cancel(task.id)
        workManager.cancelAllWorkByTag(task.id.toString())
    }

    override suspend fun cancel(list: List<Task>) {
        supervisorScope {
            for (task in list) {
                launch { cancel(task) }
            }
        }
    }
}
