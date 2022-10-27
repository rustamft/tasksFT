package com.rustamft.tasksft.notification.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.usecase.GetTaskByIdUseCase
import com.rustamft.tasksft.domain.usecase.SaveTaskUseCase
import com.rustamft.tasksft.presentation.util.NOTIFICATION_ACTION_FINISH_TASK
import com.rustamft.tasksft.presentation.util.NOTIFICATION_ACTION_REPEAT_TASK
import com.rustamft.tasksft.presentation.util.NOTIFICATION_ACTION_SNOOZE_TASK
import com.rustamft.tasksft.presentation.util.ONE_HOUR
import com.rustamft.tasksft.presentation.util.TAG_COROUTINE_EXCEPTION
import com.rustamft.tasksft.presentation.util.TASK_ID
import com.rustamft.tasksft.presentation.util.toTimeDifference
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject
import java.util.Calendar

class TaskBroadcastReceiver : BroadcastReceiver() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val getTaskByIdUseCase: GetTaskByIdUseCase by inject(GetTaskByIdUseCase::class.java)
    private val saveTaskUseCase: SaveTaskUseCase by inject(SaveTaskUseCase::class.java)

    override fun onReceive(context: Context?, intent: Intent?) {
        val pendingResult = goAsync()
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG_COROUTINE_EXCEPTION, Log.getStackTraceString(throwable))
            CoroutineScope(Dispatchers.IO).launch {
                context.displayToast(throwable.message.toString())
                pendingResult.finish()
            }
        }
        scope.launch(exceptionHandler) {
            intent?.extras?.getInt(TASK_ID)?.let { id ->
                val task = getTaskByIdUseCase.execute(taskId = id).first()
                when (intent.action) {
                    NOTIFICATION_ACTION_FINISH_TASK -> {
                        finishTask(task = task)
                    }
                    NOTIFICATION_ACTION_SNOOZE_TASK -> {
                        snoozeTask(task = task)
                        context.displayToast(R.string.notification_snoozed)
                    }
                    NOTIFICATION_ACTION_REPEAT_TASK -> {
                        if (task.repeatCalendarUnit != 0) {
                            val calendar = Calendar.getInstance().apply {
                                timeInMillis = task.reminder
                                add(task.repeatCalendarUnit, 1)
                            }
                            val updatedTask = task.copy(reminder = calendar.timeInMillis)
                            launch { saveTaskUseCase.execute(updatedTask) }
                            val difference = updatedTask.reminder.toTimeDifference()
                            context.displayToast(
                                R.string.reminder_in,
                                difference.days,
                                difference.hours,
                                difference.minutes
                            )
                        }
                    }
                }
                context?.let { NotificationManagerCompat.from(it).cancel(id) }
            }
            pendingResult.finish()
        }
    }

    private suspend fun finishTask(task: Task) {
        saveTaskUseCase.execute(task = task.copy(finished = true))
    }

    private suspend fun snoozeTask(task: Task) {
        saveTaskUseCase.execute(
            task = task.copy(reminder = System.currentTimeMillis() + ONE_HOUR)
        )
    }

    private suspend fun Context?.displayToast(stringResId: Int, vararg args: Any) {
        this?.let { displayToast(getString(stringResId, *args)) }
    }

    private suspend fun Context?.displayToast(text: String) {
        this?.let {
            withContext(Dispatchers.Main) {
                Toast.makeText(it, text, Toast.LENGTH_LONG).show()
            }
        }
    }
}
