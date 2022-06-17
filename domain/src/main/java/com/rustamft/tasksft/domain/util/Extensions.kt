package com.rustamft.tasksft.domain.util

import com.rustamft.tasksft.domain.model.DateTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun Int.format(digits: Int) = String.format("%0${digits}d", this)

fun Long.toDateTime(): DateTime {

    val calendar = Calendar.getInstance()
    if (this != 0L) {
        calendar.timeInMillis = this
    }

    val date = "${
        String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))
    }.${
        String.format("%02d", calendar.get(Calendar.MONTH))
    }.${
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

fun DateTime.toMillis(): Long { // TODO: remove
    val formatter = SimpleDateFormat(PATTERN_DATE_TIME, Locale.getDefault())
    val millis: Long = try {
        formatter.parse("$this.date $this.time")!!.time
    } catch (e: ParseException) {
        e.printStackTrace()
        0L
    }
    return millis
}
