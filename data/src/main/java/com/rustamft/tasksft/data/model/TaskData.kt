package com.rustamft.tasksft.data.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity
internal data class TaskData(
    @PrimaryKey
    val id: Int = (Calendar.getInstance().timeInMillis % Integer.MAX_VALUE).toInt(), // TODO: remove
    val title: String = "",
    val description: String = "",
    @NonNull @ColumnInfo(name = "created")
    val created: Long = Calendar.getInstance().timeInMillis,
    @NonNull @ColumnInfo(name = "reminder")
    val reminder: Long = 0L,
    val isNew: Boolean = true, // TODO: remove?
    @NonNull @ColumnInfo(name = "is_finished")
    val isFinished: Boolean = false
)
