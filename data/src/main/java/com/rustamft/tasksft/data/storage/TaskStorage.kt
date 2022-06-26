package com.rustamft.tasksft.data.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rustamft.tasksft.data.model.TaskData
import kotlinx.coroutines.flow.Flow

@Dao
internal interface TaskStorage { // TaskRoomDao

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(taskData: TaskData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(list: List<TaskData>)

    @Delete
    suspend fun delete(task: TaskData)

    @Delete
    suspend fun delete(list: List<TaskData>)

    @Query("SELECT * FROM taskData ORDER BY is_finished DESC, reminder, created ASC")
    fun getAll(): Flow<List<TaskData>>

    @Query("SELECT * FROM taskData WHERE id = :id")
    fun getById(id: Int): Flow<TaskData>

    @Query("SELECT * FROM taskData WHERE is_finished = 1")
    suspend fun getFinished(): List<TaskData>
}
