package com.rustamft.tasksft.utils.schedule

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.rustamft.tasksft.database.entity.AppTask
import com.rustamft.tasksft.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TaskWorkManager @Inject constructor(
    @ApplicationContext private val context: Context
) : AppWorkManager {

    private val workManager = WorkManager.getInstance(context)

    override fun scheduleOneTime(task: AppTask) {
        val now = Calendar.getInstance().timeInMillis
        val delay: Long = task.millis.minus(now)
        val data = workDataOf(
            Pair(Constants.TASK_ID, task.id),
            Pair(Constants.TASK_TITLE, task.title),
            Pair(Constants.TASK_DESCRIPTION, task.description)
        )
        val work = OneTimeWorkRequestBuilder<OneTimeWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag(task.id.toString())
            .build()
        workManager.enqueue(work)
    }

    override fun cancel(task: AppTask) {
        // TODO: fix, doesn't cancel
        workManager.cancelAllWorkByTag(task.id.toString())
    }

    override suspend fun cancel(list: List<AppTask>) {
        // TODO: fix as above
        for (task in list) {
            workManager.cancelAllWorkByTag(task.id.toString())
        }
    }
}
