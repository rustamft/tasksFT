package com.rustamft.tasksft.notification.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.usecase.GetTaskUseCase
import com.rustamft.tasksft.domain.usecase.SaveTaskUseCase
import com.rustamft.tasksft.presentation.global.NOTIFICATION_ACTION_FINISH_TASK
import com.rustamft.tasksft.presentation.global.NOTIFICATION_ACTION_REPEAT_TASK
import com.rustamft.tasksft.presentation.global.NOTIFICATION_ACTION_SNOOZE_TASK
import com.rustamft.tasksft.presentation.global.ONE_HOUR
import com.rustamft.tasksft.presentation.global.TAG_COROUTINE_EXCEPTION
import com.rustamft.tasksft.presentation.global.TASK_ID
import com.rustamft.tasksft.presentation.global.toTimeDifference
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject
import java.util.Calendar

class TaskBroadcastReceiver : BroadcastReceiver() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val getTaskUseCase: GetTaskUseCase by inject(GetTaskUseCase::class.java)
    private val saveTaskUseCase: SaveTaskUseCase by inject(SaveTaskUseCase::class.java)

    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        intent ?: return
        val pendingResult = goAsync()
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG_COROUTINE_EXCEPTION, Log.getStackTraceString(throwable))
            CoroutineScope(Dispatchers.IO).launch {
                context.displayToast(throwable.message.toString())
                pendingResult.finish()
            }
        }
        scope.launch(exceptionHandler) {
            handleIntent(context = context, intent = intent)
            pendingResult.finish()
        }
    }

    private suspend fun finishTask(task: Task) {
        saveTaskUseCase.execute(task = task.copy(finished = true))
    }

    private suspend fun snoozeTask(context: Context, task: Task) {
        saveTaskUseCase.execute(task = task.copy(reminder = System.currentTimeMillis() + ONE_HOUR))
        context.displayToast(R.string.notification_snoozed)
    }

    private suspend fun repeatTask(context: Context, task: Task) {
        coroutineScope {
            if (task.repeatCalendarUnit == 0) return@coroutineScope
            val calendar = Calendar.getInstance().apply {
                timeInMillis = task.reminder
                add(task.repeatCalendarUnit, 1)
            }
            val updatedTask = task.copy(reminder = calendar.timeInMillis)
            launch { saveTaskUseCase.execute(updatedTask) }
            val difference = updatedTask.reminder.toTimeDifference()
            context.displayToast(
                stringResId = R.string.reminder_in,
                args = arrayOf(
                    difference.days,
                    difference.hours,
                    difference.minutes
                )
            )
        }
    }

    private suspend fun handleIntent(context: Context, intent: Intent) {
        coroutineScope {
            val taskId = intent.extras?.getInt(TASK_ID) ?: return@coroutineScope
            val task = getTaskUseCase.execute(taskId = taskId).first() ?: return@coroutineScope
            when (intent.action) {
                NOTIFICATION_ACTION_FINISH_TASK -> finishTask(task = task)
                NOTIFICATION_ACTION_SNOOZE_TASK -> snoozeTask(context = context, task = task)
                NOTIFICATION_ACTION_REPEAT_TASK -> repeatTask(context = context, task = task)
            }
            NotificationManagerCompat.from(context).cancel(taskId)
        }
    }

    private suspend fun Context.displayToast(stringResId: Int, vararg args: Any) {
        displayToast(getString(stringResId, *args))
    }

    private suspend fun Context.displayToast(text: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@displayToast, text, Toast.LENGTH_LONG).show()
        }
    }
}
