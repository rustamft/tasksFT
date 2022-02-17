package com.rustamft.tasksft.utils.datetime

import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateTimeUtilTest {

    @Test
    fun nextFullHourString() {
        val calendar = Calendar.getInstance()
        calendar.set(2020, 12, 31, 23, 30)
        mockkStatic(Calendar::class)
        every { Calendar.getInstance() } returns calendar
        val receivedDateTime = DateTimeUtil.nextFullHourString()
        unmockkStatic(Calendar::class)
        calendar.set(2021, 1, 1, 0, 0)
        val expectedString =
            SimpleDateFormat(receivedDateTime.pattern, Locale.getDefault()).format(calendar.time)
        assertTrue(receivedDateTime.string == expectedString)
    }

    @Test
    fun dateTimeUntil() {
        val calendar = Calendar.getInstance()
        calendar.set(2022, 6, 20, 15, 23)
        val millis = calendar.timeInMillis
        calendar.set(2022, 1, 1, 0, 0)
        mockkStatic(Calendar::class)
        every { Calendar.getInstance() } returns calendar
        val receivedDateTime = DateTimeUtil.dateTimeUntil(millis)
        unmockkStatic(Calendar::class)
        val expectedDateTime = DateTimeInt(5, 19, 15, 23)
        assertTrue(
            receivedDateTime.month == expectedDateTime.month &&
                    receivedDateTime.day == expectedDateTime.day &&
                    receivedDateTime.hour == expectedDateTime.hour &&
                    receivedDateTime.minute == expectedDateTime.minute
        )
    }
}