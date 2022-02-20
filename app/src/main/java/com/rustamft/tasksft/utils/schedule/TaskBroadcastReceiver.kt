package com.rustamft.tasksft.utils.schedule

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.rustamft.tasksft.R
import com.rustamft.tasksft.database.entity.AppTask
import com.rustamft.tasksft.database.repository.AppRepo
import com.rustamft.tasksft.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class TaskBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repo: AppRepo

    @Inject
    lateinit var workManager: AppWorkManager

    override fun onReceive(context: Context?, intent: Intent?) {
        runBlocking {
            val id = intent!!.extras!!.getInt(Constants.TASK_ID)
            val task = withContext(coroutineContext) {
                repo.getEntity(id)
            }
            when (intent.action) {
                Constants.FINISH -> {
                    finish(task)
                }
                Constants.SNOOZE -> {
                    snooze(task)
                    displayToast(context!!, context.getString(R.string.notification_snoozed))
                }
            }
            NotificationManagerCompat.from(context!!).cancel(id)
        }
    }

    private suspend fun finish(task: AppTask) {
        task.isFinished = true
        repo.update(task)
    }

    private suspend fun snooze(task: AppTask) {
        val now = Calendar.getInstance().timeInMillis
        val delay: Long = 60 * 60 * 1000 // One hour.
        task.millis = now + delay
        workManager.scheduleOneTime(task)
        repo.update(task)
    }

    private fun displayToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}