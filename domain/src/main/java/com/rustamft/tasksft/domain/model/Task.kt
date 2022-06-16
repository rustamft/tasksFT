package com.rustamft.tasksft.domain.model

import java.util.Calendar

data class Task(
    val id: Int = (Calendar.getInstance().timeInMillis % Integer.MAX_VALUE).toInt(),
    val created: Long = Calendar.getInstance().timeInMillis,
    val title: String = "",
    val description: String = "",
    val reminder: Long = 0L,
    //val isNew: Boolean = true, // TODO: remove?
    val isFinished: Boolean = false
)
