package com.rustamft.tasksft.presentation.element

import android.app.TimePickerDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.rustamft.tasksft.presentation.theme.Shapes
import com.rustamft.tasksft.presentation.util.format
import java.util.Calendar

@Composable
fun TimePickerElement(
    calendarState: State<Calendar>,
    themeResId: Int,
    onValueChange: () -> Unit
) {
    val calendar by calendarState
    fun getStringFromCalendar(): String {
        return "${
        calendar.get(Calendar.HOUR_OF_DAY).format(2)
        }:${
        calendar.get(Calendar.MINUTE).format(2)
        }"
    }

    var text by remember { mutableStateOf(getStringFromCalendar()) }
    val timePickerDialog = TimePickerDialog(
        LocalContext.current,
        themeResId,
        { _, hour: Int, minute: Int ->
            calendar.apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
            }
            text = getStringFromCalendar()
            onValueChange()
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )
    Button(
        onClick = {
            timePickerDialog.show()
        },
        content = {
            Text(text = text)
        },
        shape = Shapes.large
    )
}
