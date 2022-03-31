package com.rustamft.tasksft.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rustamft.tasksft.database.entity.Task

@Database(entities = [Task::class], version = 1)
abstract class TasksRoomDatabase : RoomDatabase() {

    abstract fun tasksDao(): TasksRoomDao

    companion object {
        @Volatile
        private var INSTANCE: TasksRoomDatabase? = null

        fun getDatabase(context: Context): TasksRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    TasksRoomDatabase::class.java,
                    "app_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
