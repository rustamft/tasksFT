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
import com.rustamft.tasksft.domain.util.ACTION_FINISH
import com.rustamft.tasksft.domain.util.ACTION_SNOOZE
import com.rustamft.tasksft.domain.util.CHANNEL_ID
import com.rustamft.tasksft.domain.util.DEEP_LINK_URI
import com.rustamft.tasksft.domain.util.TASK_DESCRIPTION
import com.rustamft.tasksft.domain.util.TASK_ID
import com.rustamft.tasksft.domain.util.TASK_TITLE
import com.rustamft.tasksft.presentation.activity.MainActivity
import com.rustamft.tasksft.presentation.notification.receiver.TaskBroadcastReceiver
import com.rustamft.tasksft.presentation.screen.destinations.EditorScreenDestination
import com.rustamft.tasksft.presentation.screen.editor.EditorScreenNavArgs

class OneTimeWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    private val data = workerParams.inputData
    private val taskId = data.getInt(TASK_ID, -1)
    private val taskTitle = data.getString(TASK_TITLE)
    private val taskDescription = data.getString(TASK_DESCRIPTION)
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
        val navArgs = EditorScreenNavArgs(taskId = taskId)
        val route = EditorScreenDestination(navArgs = navArgs).route
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            "${DEEP_LINK_URI}${route}".toUri(),
            context,
            MainActivity::class.java
        )
        val deepLinkPendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(deepLinkIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        return deepLinkPendingIntent

        /* TODO: remove
        return NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.editorFragment)
            .setArguments(bundleOf(Pair(TASK_ID, id)))
            .createPendingIntent()
        */
    }

    private fun buildFinishAction(): NotificationCompat.Action {
        val intent = Intent(context, TaskBroadcastReceiver::class.java).apply {
            action = ACTION_FINISH
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
            action = ACTION_SNOOZE
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
