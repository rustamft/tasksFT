package com.rustamft.tasksft.data.repository

import android.net.Uri
import com.rustamft.tasksft.data.storage.BackupStorage
import com.rustamft.tasksft.data.util.mapFromData
import com.rustamft.tasksft.data.util.mapToData
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

    override suspend fun saveBackup(directoryUriString: String, list: List<Task>) {
        withContext(dispatcher) {
            backupStorage.save(
                directoryUri = Uri.parse(directoryUriString),
                list = list.mapToData()
            )
        }
    }

    override fun getBackup(fileUriString: String): Flow<List<Task>> {
        return backupStorage.get(fileUri = Uri.parse(fileUriString)).mapFromData()
    }
}
