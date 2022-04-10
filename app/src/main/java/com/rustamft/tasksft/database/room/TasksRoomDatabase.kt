package com.rustamft.tasksft.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rustamft.tasksft.database.entity.Task

@Database(entities = [Task::class], version = 1)
abstract class TasksRoomDatabase : RoomDatabase() {

    abstract fun tasksDao(): TasksRoomDao
}
