package com.rustamft.tasksft.utils.datetime

import com.rustamft.tasksft.utils.DATE_PATTERN
import com.rustamft.tasksft.utils.DATE_TIME_PATTERN
import com.rustamft.tasksft.utils.TIME_PATTERN

/**
 * Contains date and time as two separate strings.
 */
class DateTimeString(
    val date: String,
    val time: String
) {
    private var _pattern = ""
    private var _string = ""
    val pattern get() = _pattern
    val string get() = _string

    init {
        if (date.isNotEmpty() && time.isNotEmpty()) {
            _pattern = DATE_TIME_PATTERN
            _string = "$date $time"
        } else if (date.isNotEmpty()) {
            _pattern = DATE_PATTERN
            _string = date
        } else if (time.isNotEmpty()) {
            _pattern = TIME_PATTERN
            _string = time
        }
    }
}
