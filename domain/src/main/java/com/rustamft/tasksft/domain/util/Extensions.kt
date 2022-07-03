package com.rustamft.tasksft.domain.util

import com.rustamft.tasksft.domain.model.DateTime
import com.rustamft.tasksft.domain.model.TimeUntil
import java.util.Calendar
import java.util.Locale

fun Int.format(digits: Int) = String.format("%0${digits}d", this)

fun Long.toDateTime(): DateTime {

    val calendar = Calendar.getInstance()
    if (this != 0L) {
        calendar.timeInMillis = this
    }

    val date = "${
        calendar.get(Calendar.DAY_OF_MONTH)
    } ${
        calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
    } ${
        calendar.get(Calendar.YEAR)
    }"
    val time = "${
        String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY))
    }:${
        String.format("%02d", calendar.get(Calendar.MINUTE))
    }"

    return DateTime(
        date = date,
        time = time
    )
}

fun Long.toTimeUntil(): TimeUntil {
    with(
        Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis() - this@toTimeUntil
        }
    ) {
        return TimeUntil(
            months = get(Calendar.MONTH),
            days = get(Calendar.DAY_OF_MONTH) - 1,
            hours = get(Calendar.HOUR_OF_DAY),
            minutes = get(Calendar.MINUTE)
        )
    }
}
