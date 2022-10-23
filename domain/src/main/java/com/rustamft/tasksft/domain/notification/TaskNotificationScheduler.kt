package com.rustamft.tasksft.domain.notification

import com.rustamft.tasksft.domain.model.Task

interface TaskNotificationScheduler {

    fun schedule(task: Task)
    suspend fun schedule(list: List<Task>)
    fun cancel(task: Task)
    suspend fun cancel(list: List<Task>)
}
