package com.rustamft.tasksft.presentation.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.rustamft.tasksft.domain.model.Task
import java.util.Calendar

class MutableTask {

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

    fun setFieldsFromTask(task: Task) {
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
    }

    fun toTask(): Task {
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
                }
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
                }
            )
        }
    }
}
