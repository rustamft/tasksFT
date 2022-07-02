package com.rustamft.tasksft.domain.notification

import com.rustamft.tasksft.domain.model.Task

interface TaskWorkManager {

    fun scheduleOneTime(task: Task)
    suspend fun scheduleOneTime(list: List<Task>)
    fun cancel(task: Task)
    suspend fun cancel(list: List<Task>)
}
