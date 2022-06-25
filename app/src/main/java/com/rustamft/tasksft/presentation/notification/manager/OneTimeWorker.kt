package com.rustamft.tasksft.presentation.notification.manager

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.rustamft.tasksft.R
import com.rustamft.tasksft.presentation.notification.receiver.TaskBroadcastReceiver

class OneTimeWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    private val data = workerParams.inputData
    private val id = data.getInt(TASK_ID, -1)
    private val title = data.getString(TASK_TITLE)
    private val description = data.getString(TASK_DESCRIPTION)
    private val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.FLAG_UPDATE_CURRENT and PendingIntent.FLAG_IMMUTABLE
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }

    override fun doWork(): Result {
        displayNotification()
        return Result.success()
    }

    private fun displayNotification() {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_event)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(buildMainPendingIntent())
            .addAction(buildFinishAction())
            .addAction(buildSnoozeAction())
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(context)) {
            notify(id, builder.build())
        }
    }

    private fun buildMainPendingIntent(): PendingIntent {
        return NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.editorFragment)
            .setArguments(bundleOf(Pair(TASK_ID, id)))
            .createPendingIntent()
    }

    private fun buildFinishAction(): NotificationCompat.Action {
        val intent = Intent(context, TaskBroadcastReceiver::class.java).apply {
            action = FINISH
            putExtra(TASK_ID, id)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
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
            action = SNOOZE
            putExtra(TASK_ID, id)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
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
