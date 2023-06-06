package com.rustamft.tasksft.domain.model


data class Task(
    val id: Int,
    val created: Long,
    val title: String,
    val description: String,
    val reminder: Long,
    val repeatCalendarUnit: Int,
    val finished: Boolean,
    val color: Int
)
