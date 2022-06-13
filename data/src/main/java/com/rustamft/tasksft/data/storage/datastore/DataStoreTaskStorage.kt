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

    override suspend fun save(taskData: TaskData) {
        dataStore.updateData { dataContainer ->
            val list = dataContainer.listOfTaskData.toMutableList()
            list.add(taskData)
            dataContainer.copy(listOfTaskData = list)
        }
    }

    override suspend fun delete(list: List<TaskData>) {
        dataStore.updateData { dataContainer ->
            val mutableList = dataContainer.listOfTaskData.toMutableList()
            mutableList.removeAll(list)
            dataContainer.copy(listOfTaskData = mutableList)
        }
    }

    override fun getAll(): Flow<List<TaskData>> = dataStore.data.map { it.listOfTaskData }
}
