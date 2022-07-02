package com.rustamft.tasksft.domain.model

data class AppPreferences(
    val darkTheme: Boolean = true,
    val backupDirectory: String = ""
)
