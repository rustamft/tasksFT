package com.rustamft.tasksft.presentation.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.util.NOTIFICATION_CHANNEL_ID_BACKUP
import com.rustamft.tasksft.domain.util.NOTIFICATION_CHANNEL_ID_TASK
import com.rustamft.tasksft.presentation.composable.MainComposable

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        setContent { MainComposable() }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    /**
     * Creates the NotificationChannel
     * API 26+ required because the NotificationChannel class is new and not in the support library
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannels(
            listOf(
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ID_TASK,
                    getString(R.string.task_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = getString(R.string.task_notification_channel_description)
                },
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ID_BACKUP,
                    "Backup service notification", // TODO: extract to strings xml
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "Shows backup service notification" // TODO: extract to strings xml
                }
            )
        )
    }
}
