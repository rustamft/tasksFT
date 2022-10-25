package com.rustamft.tasksft.presentation.util.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.presentation.theme.AppTheme
import java.util.Calendar
import java.util.concurrent.TimeUnit

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
    val repeatIntervalState: MutableState<Long> = mutableStateOf(0),
    colorState: MutableState<Color> = mutableStateOf(AppTheme.taskColors.random())
) {

    private var id by idState
    private var repeatInterval by repeatIntervalState
    var title by titleState
    var description by descriptionState
    var created by createdState
    var reminderIsSet by reminderIsSetState
    var reminderCalendar by reminderCalendarState
    var color by colorState
    val reminderRepeatIntervalMap = mapOf(
        0L to UIText.StringResource(R.string.reminder_one_time),
        TimeUnit.DAYS.toMillis(1) to UIText.StringResource(R.string.reminder_every_day),
        TimeUnit.DAYS.toMillis(7) to UIText.StringResource(R.string.reminder_every_week),
        TimeUnit.DAYS.toMillis(30) to UIText.StringResource(R.string.reminder_every_month)
    ) // TODO: find better solution for months

    fun setStateFromTask(task: Task) {
        id = task.id
        title = task.title
        description = task.description
        created = task.created
        if (task.reminder > 0L) {
            reminderIsSet = true
            reminderCalendar = Calendar.getInstance().apply {
                timeInMillis = task.reminder
            }
            repeatInterval = task.repeat
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
                repeat = repeatInterval,
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
                repeat = repeatInterval,
                color = color.toArgb()
            )
        }
    }
}
