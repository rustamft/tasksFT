package com.rustamft.tasksft.data.model

import java.util.Calendar

internal data class TaskData(
    val id: Int = (Calendar.getInstance().timeInMillis % Integer.MAX_VALUE).toInt(), // TODO: remove
    val title: String = "",
    val description: String = "",
    val created: Long = Calendar.getInstance().timeInMillis,
    val reminder: Long = 0L,
    val isNew: Boolean = true, // TODO: remove?
    val isFinished: Boolean = false
)
