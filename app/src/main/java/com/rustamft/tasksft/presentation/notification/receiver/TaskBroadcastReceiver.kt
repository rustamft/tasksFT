package com.rustamft.tasksft.presentation.notification.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.usecase.GetTaskByIdUseCase
import com.rustamft.tasksft.domain.usecase.SaveTaskUseCase
import com.rustamft.tasksft.domain.util.ACTION_FINISH
import com.rustamft.tasksft.domain.util.ACTION_SNOOZE
import com.rustamft.tasksft.domain.util.ONE_HOUR
import com.rustamft.tasksft.domain.util.TASK_ID
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent.inject

class TaskBroadcastReceiver : BroadcastReceiver() {

    private val context: Context by inject(Context::class.java)
    private val getTaskByIdUseCase: GetTaskByIdUseCase by inject(GetTaskByIdUseCase::class.java)
    private val saveTaskUseCase: SaveTaskUseCase by inject(SaveTaskUseCase::class.java)
    private lateinit var task: Task

    override fun onReceive(contextNullable: Context?, intent: Intent?) {
        val pendingResult = goAsync()
        runBlocking {
            val id = intent?.extras?.getInt(TASK_ID)
            if (id != null) {
                task = getTaskByIdUseCase.execute(taskId = id).first()!!
                when (intent.action) {
                    ACTION_FINISH -> {
                        finishTask()
                    }
                    ACTION_SNOOZE -> {
                        snoozeTask()
                        displayToast(context.getString(R.string.notification_snoozed))
                    }
                }
                NotificationManagerCompat.from(context).cancel(id)
            }
            pendingResult.finish()
        }
    }

    private fun displayToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private suspend fun finishTask() {
        runCatching {
            saveTaskUseCase.execute(task = task.copy(finished = true))
        }.onFailure {
            displayToast(it.message.toString())
        }
    }

    private suspend fun snoozeTask() {
        runCatching {
            val now = System.currentTimeMillis()
            val delay: Long = ONE_HOUR
            saveTaskUseCase.execute(task = task.copy(reminder = now + delay))
        }.onFailure {
            displayToast(it.message.toString())
        }
    }
}
