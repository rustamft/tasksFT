package com.rustamft.tasksft.data.storage.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rustamft.tasksft.data.model.TaskData
import com.rustamft.tasksft.data.storage.TaskStorage

@Database(entities = [TaskData::class], version = 1)
internal abstract class TaskRoomDatabase : RoomDatabase() {

    abstract fun tasksDao(): TaskStorage
}
