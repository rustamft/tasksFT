package com.rustamft.tasksft.presentation.element

import android.app.DatePickerDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar
import java.util.Locale

@Composable
fun DatePickerElement(
    calendarState: State<Calendar>,
    themeResId: Int,
    onValueChange: () -> Unit
) {
    val calendar by calendarState
    fun getStringFromCalendar(): String {
        return "${
            calendar.get(Calendar.DAY_OF_MONTH)
        } ${
            calendar.getDisplayName(
                Calendar.MONTH,
                Calendar.LONG,
                Locale.getDefault()
            )
        } ${
            calendar.get(Calendar.YEAR)
        }"
    }

    var text by remember { mutableStateOf(getStringFromCalendar()) }
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        themeResId,
        { _, year: Int, month: Int, day: Int ->
            calendar.set(year, month, day)
            text = getStringFromCalendar()
            onValueChange()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    Button(
        onClick = {
            datePickerDialog.show()
        },
        content = {
            Text(text = text)
        }
    )
}
