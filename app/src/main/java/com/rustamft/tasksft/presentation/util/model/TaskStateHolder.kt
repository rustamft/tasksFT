package com.rustamft.tasksft.presentation.util.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.presentation.theme.AppTheme
import java.util.Calendar

class TaskStateHolder(
    idState: MutableState<Int> = mutableStateOf(-1),
    titleState: MutableState<String> = mutableStateOf(""),
    descriptionState: MutableState<String> = mutableStateOf(""),
    createdState: MutableState<Long> = mutableStateOf(0L),
    reminderIsSetState: MutableState<Boolean> = mutableStateOf(false),
    reminderCalendarState: MutableState<Calendar> = mutableStateOf(
        Calendar.getInstance().apply {
            set(Calendar.MINUTE, 0)
            add(Calendar.HOUR_OF_DAY, 1)
        }
    ),
    colorState: MutableState<Color> = mutableStateOf(AppTheme.taskColors.random())
) {

    private var id by idState
    var title by titleState
    var description by descriptionState
    var created by createdState
    var reminderIsSet by reminderIsSetState
    var reminderCalendar: Calendar by reminderCalendarState
    var color by colorState

    fun setStateFromTask(task: Task) {
        id = task.id
        title = task.title
        description = task.description
        created = task.created
        if (task.reminder != 0L) {
            reminderIsSet = true
            reminderCalendar = Calendar.getInstance().apply {
                timeInMillis = task.reminder
            }
        }
        color = Color(task.color)
    }

    fun getStateAsTask(): Task {
        return if (id == -1) { // Creating new task
            val now = System.currentTimeMillis()
            Task(
                id = (now % Integer.MAX_VALUE).toInt(),
                created = now,
                title = title,
                description = description,
                reminder = if (reminderIsSet) {
                    reminderCalendar.timeInMillis
                } else {
                    0L
                },
                color = color.toArgb()
            )
        } else { // Updating existing task
            Task(
                id = id,
                created = created,
                title = title,
                description = description,
                reminder = if (reminderIsSet) {
                    reminderCalendar.timeInMillis
                } else {
                    0L
                },
                color = color.toArgb()
            )
        }
    }
}
