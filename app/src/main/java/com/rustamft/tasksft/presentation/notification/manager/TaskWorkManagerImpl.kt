package com.rustamft.tasksft.presentation.notification.manager

import androidx.core.app.NotificationManagerCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.notification.TaskWorkManager
import com.rustamft.tasksft.domain.util.TASK_DESCRIPTION
import com.rustamft.tasksft.domain.util.TASK_ID
import com.rustamft.tasksft.domain.util.TASK_TITLE
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class TaskWorkManagerImpl(
    private val workManager: WorkManager,
    private val notificationManager: NotificationManagerCompat
) : TaskWorkManager {

    override fun scheduleOneTime(task: Task) {
        val now = System.currentTimeMillis()
        val delay: Long = task.reminder.minus(now)
        val data = workDataOf(
            Pair(TASK_ID, task.id),
            Pair(TASK_TITLE, task.title),
            Pair(TASK_DESCRIPTION, task.description)
        )
        val work = OneTimeWorkRequestBuilder<OneTimeWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag(task.id.toString())
            .build()
        workManager.enqueue(work)
    }

    override suspend fun scheduleOneTime(list: List<Task>) {
        coroutineScope {
            for (task in list) {
                launch { scheduleOneTime(task) }
            }
        }
    }


    override fun cancel(task: Task) {
        notificationManager.cancel(task.id)
        workManager.cancelAllWorkByTag(task.id.toString())
    }

    override suspend fun cancel(list: List<Task>) {
        coroutineScope {
            for (task in list) {
                launch { cancel(task) }
            }
        }
    }
}
