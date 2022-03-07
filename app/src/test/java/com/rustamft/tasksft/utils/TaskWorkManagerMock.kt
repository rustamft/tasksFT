package com.rustamft.tasksft.utils

import com.rustamft.tasksft.database.entity.AppTask
import com.rustamft.tasksft.utils.schedule.AppWorkManager

class TaskWorkManagerMock : AppWorkManager {

    var scheduledMillis: Long = -1

    override fun scheduleOneTime(task: AppTask) {
        scheduledMillis = task.reminder
    }

    override fun cancel(task: AppTask) {
        scheduledMillis = 0
    }

    override suspend fun cancel(list: List<AppTask>) {
        //
    }
}
