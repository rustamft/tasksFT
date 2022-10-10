package com.rustamft.tasksft.presentation.util.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.presentation.theme.AppTheme
import java.util.Calendar

class TaskStateHolder {

    private var id by mutableStateOf(-1)
    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var created by mutableStateOf(0L)
    var reminderIsSet by mutableStateOf(false)
    var reminderCalendar: Calendar by mutableStateOf(
        Calendar.getInstance().apply {
            set(Calendar.MINUTE, 0)
            add(Calendar.HOUR_OF_DAY, 1)
        }
    )
    var color by mutableStateOf(AppTheme.taskColors.random())

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
