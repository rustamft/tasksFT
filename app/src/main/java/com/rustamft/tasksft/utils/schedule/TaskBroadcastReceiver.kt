package com.rustamft.tasksft.utils.schedule

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.rustamft.tasksft.R
import com.rustamft.tasksft.database.entity.Task
import com.rustamft.tasksft.database.repository.TasksRepo
import com.rustamft.tasksft.utils.FINISH
import com.rustamft.tasksft.utils.SNOOZE
import com.rustamft.tasksft.utils.TASK_ID
import com.rustamft.tasksft.utils.datetime.DateTimeProvider
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class TaskBroadcastReceiver : BroadcastReceiver() {

    @Inject
    @ApplicationContext
    lateinit var context: Context

    @Inject
    lateinit var repo: TasksRepo

    lateinit var task: Task

    override fun onReceive(contextNullable: Context?, intent: Intent?) {
        val pendingResult = goAsync()
        runBlocking {
            val id = intent?.extras?.getInt(TASK_ID)
            if (id != null) {
                task = repo.getTask(id)
                when (intent.action) {
                    FINISH -> {
                        finishTask()
                    }
                    SNOOZE -> {
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
        task.isFinished = true
        repo.update(task)
    }

    private suspend fun snoozeTask() {
        val now = DateTimeProvider.getNowInMillis()
        val delay: Long = DateTimeProvider.ONE_HOUR // One hour.
        task.reminder = now + delay
        repo.update(task)
    }
}
