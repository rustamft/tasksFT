package com.rustamft.tasksft.data.storage.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rustamft.tasksft.data.model.TaskData

@Database(entities = [TaskData::class], version = 1)
internal abstract class TaskRoomDatabase : RoomDatabase() {

    abstract fun taskRoomDao(): TaskRoomDao
}
