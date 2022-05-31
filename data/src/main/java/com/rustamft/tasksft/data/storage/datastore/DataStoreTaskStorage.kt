package com.rustamft.tasksft.data.storage.datastore

import androidx.datastore.core.DataStore
import com.rustamft.tasksft.data.model.TaskData
import com.rustamft.tasksft.data.model.container.DataContainer
import com.rustamft.tasksft.data.storage.TaskStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class DataStoreTaskStorage(
    private val dataStore: DataStore<DataContainer>
) : TaskStorage {

    override suspend fun save(id: Int, taskData: TaskData) {
        dataStore.updateData {
            val mapOfTaskData = it.mapOfTaskData.toMutableMap()
            mapOfTaskData[id] = taskData
            it.copy(mapOfTaskData = mapOfTaskData)
        }
    }

    override fun getById(id: Int): Flow<TaskData> {
        return dataStore.data.map { it.mapOfTaskData[id] ?: TaskData() }
    }

    override fun getAll(): Flow<Map<Int, TaskData>> = dataStore.data.map { it.mapOfTaskData }
}
