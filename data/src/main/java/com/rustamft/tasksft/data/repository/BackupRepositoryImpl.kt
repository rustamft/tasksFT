package com.rustamft.tasksft.data.repository

import android.net.Uri
import com.rustamft.tasksft.data.model.map
import com.rustamft.tasksft.data.storage.BackupStorage
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.repository.BackupRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

internal class BackupRepositoryImpl(
    private val backupStorage: BackupStorage,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BackupRepository {

    override suspend fun save(
        fileName: String,
        tasks: List<Task>,
        directoryUriString: String
    ) {
        withContext(dispatcher) {
            backupStorage.save(
                fileName = fileName,
                tasks = tasks.map(),
                directoryUri = Uri.parse(directoryUriString)
            )
        }
    }

    override fun get(fileUriString: String): Flow<List<Task>> {
        return backupStorage.get(fileUri = Uri.parse(fileUriString)).map()
    }
}
