package com.rustamft.tasksft.data.storage.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rustamft.tasksft.data.model.TaskData

@Database(
    version = 1,
    entities = [TaskData::class],
)
internal abstract class TaskRoomDatabase : RoomDatabase() {

    abstract fun taskRoomDao(): TaskRoomDao
}
