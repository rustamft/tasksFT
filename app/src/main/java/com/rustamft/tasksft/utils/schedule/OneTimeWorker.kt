package com.rustamft.tasksft.utils.schedule

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.rustamft.tasksft.R
import com.rustamft.tasksft.activities.MainActivity
import com.rustamft.tasksft.utils.Const
import kotlin.random.Random

class OneTimeWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {

        displayNotification()

        return Result.success()
    }

    private fun displayNotification() {
        // TODO: Implement notification buttons.

        val data = workerParams.inputData
        val id = data.getInt(Const.TASK_ID, -1)
        val title = data.getString(Const.TASK_TITLE)
        val description = data.getString(Const.TASK_DESCRIPTION)

        /*
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(Const.TASK_ID, id)
        }

        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT and PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, flags)
        */

        val pendingIntent = NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.editorFragment)
            .setArguments(bundleOf(Pair(Const.TASK_ID, id)))
            .createPendingIntent()

        val builder = NotificationCompat.Builder(context, Const.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_checkbox_checked)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(Random.nextInt(), builder.build())
        }
    }
}