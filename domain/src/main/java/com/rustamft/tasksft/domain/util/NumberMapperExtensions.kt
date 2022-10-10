package com.rustamft.tasksft.domain.util

import com.rustamft.tasksft.domain.util.model.DateTime
import com.rustamft.tasksft.domain.util.model.TimeDifference
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

fun Long.toTimeDifference(): TimeDifference { // TODO: buggy, test and fix
    val currentCalendar = Calendar.getInstance()
    val difference = this - currentCalendar.timeInMillis
    if (difference <= 0L) {
        return TimeDifference(0, 0, 0, 0)
    } else {
        val futureCalendar = Calendar.getInstance()
        futureCalendar.timeInMillis = this
        var diffMonth =
            futureCalendar.get(Calendar.MONTH) - currentCalendar.get(Calendar.MONTH)
        if (diffMonth < 0) {
            diffMonth += 12
        }
        var diffDay =
            futureCalendar.get(Calendar.DAY_OF_MONTH) - currentCalendar.get(Calendar.DAY_OF_MONTH)
        if (diffDay < 0) {
            diffMonth--
            diffDay += futureCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        }
        var diffHour =
            futureCalendar.get(Calendar.HOUR_OF_DAY) - currentCalendar.get(Calendar.HOUR_OF_DAY)
        if (diffHour < 0) {
            diffDay--
            diffHour += 24
        }
        var diffMin =
            futureCalendar.get(Calendar.MINUTE) - currentCalendar.get(Calendar.MINUTE)
        if (diffMin < 0) {
            diffHour--
            diffMin += 60
        }
        return TimeDifference(diffMonth, diffDay, diffHour, diffMin)
    }
}
