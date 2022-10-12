package com.rustamft.tasksft.presentation.notification.receiver

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
import com.rustamft.tasksft.presentation.util.NOTIFICATION_ACTION_SNOOZE_TASK
import com.rustamft.tasksft.presentation.util.ONE_HOUR
import com.rustamft.tasksft.presentation.util.TAG_COROUTINE_EXCEPTION
import com.rustamft.tasksft.presentation.util.TASK_ID
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class TaskBroadcastReceiver : BroadcastReceiver() {

    private val getTaskByIdUseCase: GetTaskByIdUseCase by inject(GetTaskByIdUseCase::class.java)
    private val saveTaskUseCase: SaveTaskUseCase by inject(SaveTaskUseCase::class.java)
    private var task: Task? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch(
            CoroutineExceptionHandler { _, throwable ->
                Log.e(TAG_COROUTINE_EXCEPTION, Log.getStackTraceString(throwable))
                CoroutineScope(Dispatchers.IO).launch {
                    context.displayToast(throwable.message.toString())
                    pendingResult.finish()
                }
            }
        ) {
            intent?.extras?.getInt(TASK_ID)?.let { id ->
                task = getTaskByIdUseCase.execute(taskId = id).first()
                when (intent.action) {
                    NOTIFICATION_ACTION_FINISH_TASK -> {
                        finishTask()
                    }
                    NOTIFICATION_ACTION_SNOOZE_TASK -> {
                        snoozeTask()
                        context.displayToast(R.string.notification_snoozed)
                    }
                }
                context?.let { NotificationManagerCompat.from(it).cancel(id) }
            }
            pendingResult.finish()
        }
    }

    private suspend fun finishTask() {
        task?.let { saveTaskUseCase.execute(task = it.copy(finished = true)) }
    }

    private suspend fun snoozeTask() {
        task?.let {
            saveTaskUseCase.execute(
                task = it.copy(reminder = System.currentTimeMillis() + ONE_HOUR)
            )
        }
    }

    private suspend fun Context?.displayToast(stringResId: Int) {
        this?.let { displayToast(getString(stringResId)) }
    }

    private suspend fun Context?.displayToast(text: String) {
        this?.let {
            withContext(Dispatchers.Main) {
                Toast.makeText(it, text, Toast.LENGTH_LONG).show()
            }
        }
    }
}
