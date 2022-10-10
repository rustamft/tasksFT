package com.rustamft.tasksft.presentation.util

internal fun Int.format(digits: Int) = String.format("%0${digits}d", this)
