package com.rustamft.tasksft.database.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rustamft.tasksft.database.entity.AppTask
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksRoomDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: AppTask)

    @Update
    suspend fun update(task: AppTask)

    @Delete
    suspend fun delete(task: AppTask)

    @Delete
    suspend fun delete(list: List<AppTask>)

    @Query("SELECT * FROM apptask ORDER BY millis ASC")
    fun getAll(): Flow<List<AppTask>>

    @Query("SELECT id FROM apptask")
    suspend fun getIds(): List<Int>

    @Query("SELECT * FROM apptask WHERE is_finished = 1")
    suspend fun getFinished(): List<AppTask>

    @Query("SELECT * FROM apptask WHERE id = :id")
    suspend fun getEntity(id: Int): AppTask
}
