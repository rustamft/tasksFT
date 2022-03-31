package com.rustamft.tasksft.utils.datetime

import com.rustamft.tasksft.utils.DATE_PATTERN
import com.rustamft.tasksft.utils.TIME_PATTERN
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateTimeProvider {

    const val ONE_HOUR = 60L * 60L * 1000L

    fun getNowInMillis(): Long {
        return Calendar.getInstance().timeInMillis
    }

    fun getNowAsString(): String {
        return millisToDateTimeString(getNowInMillis()).string
    }

    fun isInFuture(millis: Long): Boolean {
        return if (millis == 0L) {
            false
        } else {
            val calendar = Calendar.getInstance()
            millis > calendar.timeInMillis
        }
    }

    /**
     * Converts DateTimeString to milliseconds.
     */
    fun stringToMillis(dateTime: DateTimeString): Long {
        val formatter = SimpleDateFormat(dateTime.pattern, Locale.getDefault())
        val millis: Long = try {
            formatter.parse(dateTime.string)!!.time
        } catch (e: ParseException) {
            e.printStackTrace()
            0L
        }
        return millis
    }

    /**
     * Returns next full hour as DateTimeString.
     */
    fun nextFullHourAsDateTimeString(): DateTimeString {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MINUTE, 0)
        calendar.roll(Calendar.HOUR_OF_DAY, true)
        if (calendar.get(Calendar.HOUR_OF_DAY) == 0) {
            calendar.roll(Calendar.DAY_OF_MONTH, true)
            if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
                calendar.roll(Calendar.MONTH, true)
                if (calendar.get(Calendar.MONTH) == 0) {
                    calendar.roll(Calendar.YEAR, true)
                }
            }
        }
        return DateTimeString(
            formatDate(calendar.time),
            formatTime(calendar.time)
        )
    }

    /**
     * Converts milliseconds to DateTimeString.
     * @param millis input milliseconds.
     */
    fun millisToDateTimeString(millis: Long): DateTimeString {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        return DateTimeString(
            formatDate(calendar.time),
            formatTime(calendar.time)
        )
    }

    /**
     * Converts the given ints of date parts to a date string.
     */
    fun intToDateString(date: DateTimeInt): String {
        val calendar = Calendar.getInstance()
        calendar.set(date.year, date.month, date.day)
        return formatDate(calendar.time)
    }

    /**
     * Converts the given ints of time parts to a time string.
     */
    fun intToTimeString(time: DateTimeInt): String {
        val calendar = Calendar.getInstance()
        calendar.set(0, 0, 0, time.hour, time.minute)
        return formatTime(calendar.time)
    }

    /**
     * Returns DateTimeInt left until reminder is shown.
     */
    fun dateTimeUntil(millis: Long): DateTimeInt {
        val currentCalendar = Calendar.getInstance()
        val difference = millis - currentCalendar.timeInMillis
        if (difference <= 0L) {
            return DateTimeInt(0, 0, 0, 0)
        } else {
            val futureCalendar = Calendar.getInstance()
            futureCalendar.timeInMillis = millis
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
            return DateTimeInt(diffMonth, diffDay, diffHour, diffMin)
        }
    }

    private fun formatDate(date: Date): String {
        val formatter = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
        return formatter.format(date)
    }

    private fun formatTime(time: Date): String {
        val formatter = SimpleDateFormat(TIME_PATTERN, Locale.getDefault())
        return formatter.format(time)
    }
}
