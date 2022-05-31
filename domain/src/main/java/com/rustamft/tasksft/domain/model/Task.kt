package com.rustamft.tasksft.domain.model

import java.util.Calendar

data class Task(
    val id: Int = (Calendar.getInstance().timeInMillis % Integer.MAX_VALUE).toInt(), // TODO: remove
    var title: String = "",
    var description: String = "",
    val created: Long = Calendar.getInstance().timeInMillis,
    var reminder: Long = 0L,
    var isNew: Boolean = true,
    var isFinished: Boolean = false
)
