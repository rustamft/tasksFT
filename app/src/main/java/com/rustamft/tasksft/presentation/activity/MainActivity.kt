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
import androidx.compose.material.rememberScaffoldState
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.util.CHANNEL_ID
import com.rustamft.tasksft.presentation.screen.NavGraphs
import com.rustamft.tasksft.presentation.theme.AppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        setContent {

            val scaffoldState = rememberScaffoldState()
            val navController = rememberNavController()

            AppTheme {
                DestinationsNavHost(
                    navGraph = NavGraphs.root,
                    navController = navController,
                    dependenciesContainerBuilder = {
                        dependency(scaffoldState)
                    }
                )
            }
        }
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
        val name = getString(R.string.task_notification_channel_name)
        val descriptionText = getString(R.string.task_notification_channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
