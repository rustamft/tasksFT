package com.rustamft.tasksft.domain.repository

import com.rustamft.tasksft.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface BackupRepository {

    suspend fun saveBackup(directoryUriString: String, list: List<Task>)
    fun getBackup(fileUriString: String): Flow<List<Task>>
}
