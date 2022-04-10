package com.rustamft.tasksft.database.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity
data class Task(
    @PrimaryKey
    val id: Int = (Calendar.getInstance().timeInMillis % Integer.MAX_VALUE).toInt(),
    var title: String = "",
    var description: String = "",
    @NonNull @ColumnInfo(name = "created")
    val created: Long = Calendar.getInstance().timeInMillis,
    @NonNull @ColumnInfo(name = "reminder")
    var reminder: Long = 0L,
    var isNew: Boolean = true,
    @NonNull @ColumnInfo(name = "is_finished")
    var isFinished: Boolean = false
)
