package com.rustamft.tasksft.data.storage.datastore

import androidx.datastore.core.DataStore
import com.rustamft.tasksft.data.model.TaskData
import com.rustamft.tasksft.data.model.container.DataContainer
import com.rustamft.tasksft.data.storage.TaskStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException

internal class DataStoreTaskStorage(
    private val dataStore: DataStore<DataContainer>
) : TaskStorage {

    @Throws(IOException::class, Exception::class)
    override suspend fun save(taskData: TaskData) {
        dataStore.updateData { dataContainer ->
            val list = dataContainer.listOfTaskData.toMutableList()
            val existingTask = list.find { it.id == taskData.id }
            if (existingTask == null) {
                list.add(taskData)
            } else {
                val index = list.indexOf(existingTask)
                list[index] = taskData
            }
            dataContainer.copy(listOfTaskData = list)
        }
    }

    override suspend fun delete(list: List<TaskData>) { // TODO: add exception handling
        dataStore.updateData { dataContainer ->
            val mutableList = dataContainer.listOfTaskData.toMutableList()
            mutableList.removeAll(list)
            dataContainer.copy(listOfTaskData = mutableList)
        }
    }

    override fun getAll(): Flow<List<TaskData>> = dataStore.data.map { it.listOfTaskData }
}
