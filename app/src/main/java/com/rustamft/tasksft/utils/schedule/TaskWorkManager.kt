package com.rustamft.tasksft.utils.schedule

import android.content.Context
import androidx.work.*
import com.rustamft.tasksft.database.entity.AppTask
import com.rustamft.tasksft.utils.Const
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
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
            Pair(Const.TASK_ID, task.id),
            Pair(Const.TASK_TITLE, task.title),
            Pair(Const.TASK_DESCRIPTION, task.description)
        )
        val work = OneTimeWorkRequestBuilder<OneTimeWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag(task.title)
            .build()
        workManager.enqueue(work)
    }

    override fun cancel(task: AppTask) {
        workManager.cancelAllWorkByTag(task.title)
    }

    override fun cancel(list: List<AppTask>) {
        for (task in list) {
            workManager.cancelAllWorkByTag(task.title)
        }
    }
}