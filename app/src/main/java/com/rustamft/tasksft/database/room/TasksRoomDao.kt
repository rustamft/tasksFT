package com.rustamft.tasksft.database.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rustamft.tasksft.database.entity.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksRoomDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(list: List<Task>)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Delete
    suspend fun delete(list: List<Task>)

    @Query("SELECT * FROM task ORDER BY is_finished DESC, reminder, created ASC")
    fun getTasksList(): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE is_finished = 1")
    suspend fun getFinishedTasks(): List<Task>

    @Query("SELECT * FROM task WHERE id = :id")
    suspend fun getTask(id: Int): Task
}
