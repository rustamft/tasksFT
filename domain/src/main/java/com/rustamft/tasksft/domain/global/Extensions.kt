package com.rustamft.tasksft.domain.global

import com.rustamft.tasksft.domain.model.Task

internal fun List<Task>.defaultSort() = this.sortedWith(
    compareBy(
        { !it.finished },
        { it.reminder },
        { it.created }
    )
)
