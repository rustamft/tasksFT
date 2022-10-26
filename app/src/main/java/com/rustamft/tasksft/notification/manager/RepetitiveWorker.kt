package com.rustamft.tasksft.notification.manager

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.usecase.GetTaskByIdUseCase
import com.rustamft.tasksft.domain.usecase.SaveTaskUseCase
import com.rustamft.tasksft.notification.receiver.TaskBroadcastReceiver
import com.rustamft.tasksft.presentation.activity.MainActivity
import com.rustamft.tasksft.presentation.screen.destinations.EditorScreenDestination
import com.rustamft.tasksft.presentation.util.DEEP_LINK_URI
import com.rustamft.tasksft.presentation.util.NOTIFICATION_ACTION_OK_TASK
import com.rustamft.tasksft.presentation.util.NOTIFICATION_CHANNEL_ID_TASK
import com.rustamft.tasksft.presentation.util.TASK_DESCRIPTION
import com.rustamft.tasksft.presentation.util.TASK_ID
import com.rustamft.tasksft.presentation.util.TASK_TITLE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject
import java.util.Calendar

class RepetitiveWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val data = workerParams.inputData
    private val taskId = data.getInt(TASK_ID, -1)
    private val taskTitle = data.getString(TASK_TITLE)
    private val taskDescription = data.getString(TASK_DESCRIPTION)
    private val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.FLAG_IMMUTABLE
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }
    private val getTaskByIdUseCase: GetTaskByIdUseCase by inject(GetTaskByIdUseCase::class.java)
    private val saveTaskUseCase: SaveTaskUseCase by inject(SaveTaskUseCase::class.java)

    override suspend fun doWork(): Result {
        displayNotification() // TODO not showing because of work below
        return withContext(Dispatchers.IO) {
            val task = getTaskByIdUseCase.execute(taskId).first()
            if (task.repeatCalendarUnit == 0) return@withContext Result.failure()
            val calendar = Calendar.getInstance().apply {
                timeInMillis = task.reminder
                add(task.repeatCalendarUnit, 1)
            }
            saveTaskUseCase.execute(task.copy(reminder = calendar.timeInMillis))
            return@withContext Result.success()
        }
    }

    private fun displayNotification() {
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID_TASK)
            .setSmallIcon(R.drawable.ic_event)
            .setContentTitle(taskTitle)
            .setContentText(taskDescription)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(buildMainPendingIntent())
            .addAction(buildOkAction())
            .setAutoCancel(true)
        NotificationManagerCompat.from(context).notify(taskId, notification.build())
    }

    private fun buildMainPendingIntent(): PendingIntent {
        val route = EditorScreenDestination(taskId = taskId).route
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            "$DEEP_LINK_URI${route}".toUri(),
            context,
            MainActivity::class.java
        )
        val deepLinkPendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(deepLinkIntent)
            getPendingIntent(taskId, flags)
        }
        return deepLinkPendingIntent
    }

    private fun buildOkAction(): NotificationCompat.Action {
        val intent = Intent(context, TaskBroadcastReceiver::class.java).apply {
            action = NOTIFICATION_ACTION_OK_TASK
            putExtra(TASK_ID, taskId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId,
            intent,
            flags
        )
        return NotificationCompat.Action(
            R.drawable.ic_done,
            context.getString(android.R.string.ok),
            pendingIntent
        )
    }
}
