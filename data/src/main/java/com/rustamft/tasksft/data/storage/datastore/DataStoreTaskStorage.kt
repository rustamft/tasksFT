package com.rustamft.tasksft.data.storage.datastore

import androidx.datastore.core.DataStore
import com.rustamft.tasksft.data.model.DataContainer
import com.rustamft.tasksft.data.model.TaskData
import com.rustamft.tasksft.data.storage.TaskStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class DataStoreTaskStorage(
    private val dataStore: DataStore<DataContainer>
) : TaskStorage {

    override suspend fun save(taskData: TaskData) {
        dataStore.updateData {
            it.copy(taskData = taskData)
        }
    }

    override fun get(): Flow<TaskData> = dataStore.data.map { it.taskData }
}
