package com.rustamft.tasksft.domain.model

data class Task(
    val id: Int = -1,
    val created: Long = 0L,
    val title: String = "",
    val description: String = "",
    val reminder: Long = 0L,
    val finished: Boolean = false
)
