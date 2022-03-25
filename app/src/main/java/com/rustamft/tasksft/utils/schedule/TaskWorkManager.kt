package com.rustamft.tasksft.utils.schedule

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.rustamft.tasksft.database.entity.AppTask
import com.rustamft.tasksft.utils.TASK_DESCRIPTION
import com.rustamft.tasksft.utils.TASK_ID
import com.rustamft.tasksft.utils.TASK_TITLE
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TaskWorkManager @Inject constructor(
    @ApplicationContext private val context: Context
) : AppWorkManager {

    private val workManager = WorkManager.getInstance(context)

    override fun scheduleOneTime(task: AppTask) {
        val now = Calendar.getInstance().timeInMillis
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

    override fun cancel(task: AppTask) {
        NotificationManagerCompat.from(context).cancel(task.id)
        workManager.cancelAllWorkByTag(task.id.toString())
    }

    override suspend fun cancel(list: List<AppTask>) {
        coroutineScope {
            for (task in list) {
                launch {
                    cancel(task)
                }
            }
        }
    }
}
