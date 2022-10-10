package com.rustamft.tasksft.presentation.util

import com.rustamft.tasksft.presentation.util.model.DateTime
import com.rustamft.tasksft.presentation.util.model.TimeDifference
import java.util.Calendar
import java.util.Locale
import kotlin.time.DurationUnit
import kotlin.time.toDuration

internal fun Long.toDateTime(): DateTime {

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

internal fun Long.toTimeDifference(): TimeDifference {
    val difference = this - System.currentTimeMillis()
    difference.toDuration(DurationUnit.MILLISECONDS).toComponents { days, hours, minutes, _, _ ->
        return TimeDifference(
            days.toInt(),
            hours,
            minutes
        )
    }
}
