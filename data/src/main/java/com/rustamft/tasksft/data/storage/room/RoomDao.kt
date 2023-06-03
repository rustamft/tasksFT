package com.rustamft.tasksft.data.storage.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rustamft.tasksft.data.model.PreferencesData
import com.rustamft.tasksft.data.model.TaskData
import com.rustamft.tasksft.data.storage.PreferencesStorage
import com.rustamft.tasksft.data.storage.TaskStorage
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class RoomDao : PreferencesStorage, TaskStorage {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun save(preferences: PreferencesData)

    @Query("SELECT * FROM preferences WHERE id = 0")
    abstract override fun get(): Flow<PreferencesData?> // Sends null in flow if nothing's there

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun save(task: TaskData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun save(tasks: List<TaskData>)

    @Delete
    abstract override suspend fun delete(task: TaskData)

    @Delete
    abstract override suspend fun delete(tasks: List<TaskData>)

    @Query("SELECT * FROM task ORDER BY finished DESC, reminder, created ASC")
    abstract override fun getAll(): Flow<List<TaskData>>

    @Query("SELECT * FROM task WHERE id = :taskId")
    abstract override fun get(taskId: Int): Flow<TaskData?>

    @Query("SELECT * FROM task WHERE finished = 1")
    abstract override fun getFinished(): Flow<List<TaskData>>
}
