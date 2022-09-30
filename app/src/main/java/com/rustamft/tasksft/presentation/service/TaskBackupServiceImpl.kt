package com.rustamft.tasksft.presentation.service

import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.rustamft.tasksft.domain.service.TaskBackupService
import com.rustamft.tasksft.domain.service.TaskBackupService.OperationType
import com.rustamft.tasksft.domain.util.NOTIFICATION_CHANNEL_ID_BACKUP
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskBackupServiceImpl(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TaskBackupService, LifecycleService() {

    private enum class NotificationContent(
        val icon: Int, //TODO: add icons
        val title: String,
    ) {
        EXPORT(0, "Exporting tasks"), // TODO: move to strings xml
        IMPORT(0, "Importing tasks")
    }

    private lateinit var runnableBlock: suspend () -> Unit
    private lateinit var notificationContent: NotificationContent

    override fun start(operationType: OperationType, block: suspend () -> Unit) {
        runnableBlock = block
        notificationContent = when (operationType) {
            OperationType.EXPORT -> NotificationContent.EXPORT
            OperationType.IMPORT -> NotificationContent.IMPORT
        }
        val intent = Intent(context, TaskBackupServiceImpl::class.java)
        context.startForegroundService(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        lifecycleScope.launch(dispatcher) {
            runnableBlock
            stopSelf()
        }

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID_BACKUP)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(notificationContent.icon)
            .setContentTitle(notificationContent.title)
            //.setContentText()
            .build()

        startForeground(1, notification)

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? { // TODO: check if needed
        return super.onBind(intent)
    }
}
