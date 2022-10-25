package com.rustamft.tasksft.data.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
internal data class TaskData(
    @PrimaryKey
    val id: Int = -1,
    val title: String = "",
    val description: String = "",
    @NonNull @ColumnInfo(name = "created")
    val created: Long = 0L,
    @NonNull @ColumnInfo(name = "reminder")
    val reminder: Long = 0L,
    val repeat: Long = 0L,
    @NonNull @ColumnInfo(name = "finished")
    val finished: Boolean = false,
    val color: Int = 0x00000000
)
