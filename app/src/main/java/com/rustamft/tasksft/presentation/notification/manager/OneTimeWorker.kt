package com.rustamft.tasksft.presentation.notification.manager

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.rustamft.tasksft.R
import com.rustamft.tasksft.presentation.util.NOTIFICATION_ACTION_FINISH_TASK
import com.rustamft.tasksft.presentation.util.NOTIFICATION_ACTION_SNOOZE_TASK
import com.rustamft.tasksft.presentation.util.NOTIFICATION_CHANNEL_ID_TASK
import com.rustamft.tasksft.presentation.util.DEEP_LINK_URI
import com.rustamft.tasksft.presentation.util.TASK_DESCRIPTION
import com.rustamft.tasksft.presentation.util.TASK_ID
import com.rustamft.tasksft.presentation.util.TASK_TITLE
import com.rustamft.tasksft.presentation.activity.MainActivity
import com.rustamft.tasksft.presentation.notification.receiver.TaskBroadcastReceiver
import com.rustamft.tasksft.presentation.screen.destinations.EditorScreenDestination

class OneTimeWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    private val data = workerParams.inputData
    private val taskId = data.getInt(TASK_ID, -1)
    private val taskTitle = data.getString(TASK_TITLE)
    private val taskDescription = data.getString(TASK_DESCRIPTION)
    private val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.FLAG_IMMUTABLE
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }

    override fun doWork(): Result {
        displayNotification()
        return Result.success()
    }

    private fun displayNotification() {
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID_TASK)
            .setSmallIcon(R.drawable.ic_event)
            .setContentTitle(taskTitle)
            .setContentText(taskDescription)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(buildMainPendingIntent())
            .addAction(buildFinishAction())
            .addAction(buildSnoozeAction())
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(context)) {
            notify(taskId, builder.build())
        }
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

    private fun buildFinishAction(): NotificationCompat.Action {
        val intent = Intent(context, TaskBroadcastReceiver::class.java).apply {
            action = NOTIFICATION_ACTION_FINISH_TASK
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
            context.getString(R.string.notification_finish),
            pendingIntent
        )
    }

    private fun buildSnoozeAction(): NotificationCompat.Action {
        val intent = Intent(context, TaskBroadcastReceiver::class.java).apply {
            action = NOTIFICATION_ACTION_SNOOZE_TASK
            putExtra(TASK_ID, taskId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId,
            intent,
            flags
        )
        return NotificationCompat.Action(
            R.drawable.ic_snooze,
            context.getString(R.string.notification_snooze),
            pendingIntent
        )
    }
}
