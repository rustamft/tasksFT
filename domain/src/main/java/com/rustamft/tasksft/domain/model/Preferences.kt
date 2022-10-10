package com.rustamft.tasksft.domain.model

data class Preferences(
    val id: Int = 0,
    val theme: Theme = Theme.Auto,
    val backupDirectory: String = ""
) {

    sealed interface Theme {

        object Auto : Theme
        object Light : Theme
        object Dark : Theme
    }
}
