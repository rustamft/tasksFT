package com.rustamft.tasksft.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.rustamft.tasksft.R
import com.rustamft.tasksft.utils.Const
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
    }

    /**
     * Creates the NotificationChannel.
     * API 26+ required because the NotificationChannel class is new and not in the support library.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {

        val name = getString(R.string.task_notification_name)
        val descriptionText = getString(R.string.task_notification_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(Const.CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
