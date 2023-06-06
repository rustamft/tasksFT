package com.rustamft.tasksft.presentation.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.presentation.theme.AppTheme
import java.util.Calendar

class TaskViewState(
    id: MutableState<Int> = mutableIntStateOf(-1),
    title: MutableState<String> = mutableStateOf(""),
    description: MutableState<String> = mutableStateOf(""),
    created: MutableState<Long> = mutableLongStateOf(0L),
    isReminderSet: MutableState<Boolean> = mutableStateOf(false),
    reminder: MutableState<Calendar> = mutableStateOf(
        Calendar.getInstance().apply {
            set(Calendar.MINUTE, 0)
            add(Calendar.HOUR_OF_DAY, 1)
        }
    ),
    repeatCalendarUnits: MutableState<Int> = mutableIntStateOf(0),
    color: MutableState<Color> = mutableStateOf(AppTheme.taskColors.random())
) {

    val stateRepeatCalendarUnits by lazy { repeatCalendarUnits }
    var title by title
    var description by description
    var created by created
    var isReminderSet by isReminderSet
    var reminder by reminder
    var color by color
    private var id by id
    private var repeatCalendarUnits by repeatCalendarUnits

    companion object {
        val CALENDAR_UNIT_TO_NAME = mapOf(
            0 to UIText.StringResource(R.string.reminder_one_time),
            Calendar.DAY_OF_MONTH to UIText.StringResource(R.string.reminder_daily),
            Calendar.WEEK_OF_MONTH to UIText.StringResource(R.string.reminder_weekly),
            Calendar.MONTH to UIText.StringResource(R.string.reminder_monthly)
        )
    }

    fun setStateFromTask(task: Task) {
        id = task.id
        title = task.title
        description = task.description
        created = task.created
        if (task.reminder > 0L) {
            isReminderSet = true
            reminder = Calendar.getInstance().apply {
                timeInMillis = task.reminder
            }
            repeatCalendarUnits = task.repeatCalendarUnit
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
                reminder = if (!isReminderSet) 0L else reminder.timeInMillis,
                repeatCalendarUnit = repeatCalendarUnits,
                finished = false,
                color = color.toArgb()
            )
        } else { // Updating existing task
            Task(
                id = id,
                created = created,
                title = title,
                description = description,
                reminder = if (!isReminderSet) 0L else reminder.timeInMillis,
                repeatCalendarUnit = repeatCalendarUnits,
                finished = false,
                color = color.toArgb()
            )
        }
    }
}
