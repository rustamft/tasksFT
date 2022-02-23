package com.rustamft.tasksft.utils.schedule

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
import com.rustamft.tasksft.activities.MainActivity
import com.rustamft.tasksft.utils.Constants

class OneTimeWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    private val data = workerParams.inputData
    private val id = data.getInt(Constants.TASK_ID, -1)
    private val title = data.getString(Constants.TASK_TITLE)
    private val description = data.getString(Constants.TASK_DESCRIPTION)
    private val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.FLAG_UPDATE_CURRENT and PendingIntent.FLAG_MUTABLE
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }

    override fun doWork(): Result {
        displayNotification()
        return Result.success()
    }

    private fun displayNotification() {
        val builder = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
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
            .setArguments(bundleOf(Pair(Constants.TASK_ID, id)))
            .createPendingIntent()
    }

    private fun buildFinishAction(): NotificationCompat.Action {
        val intent = Intent(context, TaskBroadcastReceiver::class.java).apply {
            action = Constants.FINISH
            putExtra(Constants.TASK_ID, id)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1,
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
            action = Constants.SNOOZE
            putExtra(Constants.TASK_ID, id)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            2,
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
