package com.rustamft.tasksft.utils

import com.rustamft.tasksft.database.entity.Task
import com.rustamft.tasksft.utils.schedule.TasksWorkManager

class TaskWorkManagerMock : TasksWorkManager {

    var scheduledMillis: Long = -1

    override fun scheduleOneTime(task: Task) {
        scheduledMillis = task.reminder
    }

    override fun cancel(task: Task) {
        scheduledMillis = 0
    }

    override suspend fun cancel(list: List<Task>) {
        //
    }
}
