package com.rustamft.tasksft.data.storage.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rustamft.tasksft.data.model.PreferencesData
import com.rustamft.tasksft.data.model.TaskData

@Database(
    version = 1,
    entities = [PreferencesData::class, TaskData::class],
)
internal abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun dao(): RoomDao

    object RoomDatabaseCreator {

        fun create(context: Context): AppRoomDatabase {
            return Room.databaseBuilder(
                context,
                AppRoomDatabase::class.java,
                "app_database"
            ).build()
        }
    }
}
