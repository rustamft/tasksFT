package com.rustamft.tasksft.data.storage

import android.net.Uri
import com.rustamft.tasksft.data.model.TaskData
import kotlinx.coroutines.flow.Flow

internal interface BackupStorage {

    suspend fun save(
        fileName: String,
        tasks: List<TaskData>,
        directoryUri: Uri
    )

    fun get(fileUri: Uri): Flow<List<TaskData>>
}
