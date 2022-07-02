package com.rustamft.tasksft.data.storage

import android.net.Uri
import com.rustamft.tasksft.data.model.TaskData
import kotlinx.coroutines.flow.Flow

internal interface BackupStorage {

    suspend fun save(directoryUri: Uri, list: List<TaskData>)
    fun get(fileUri: Uri): Flow<List<TaskData>>
}
