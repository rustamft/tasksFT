package com.rustamft.tasksft.domain.repository

import com.rustamft.tasksft.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface BackupRepository {

    suspend fun save(
        fileName: String,
        tasks: List<Task>,
        directoryUriString: String
    )

    fun get(fileUriString: String): Flow<List<Task>>
}
