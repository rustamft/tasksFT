package com.rustamft.tasksft.database.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity
data class Task(
    @PrimaryKey val id: Int,
    var title: String,
    var description: String,
    @NonNull @ColumnInfo(name = "created") val created: Long,
    @NonNull @ColumnInfo(name = "reminder") var reminder: Long,
    var isNew: Boolean,
    @NonNull @ColumnInfo(name = "is_finished") var isFinished: Boolean
) {

    constructor() : this(
        (Calendar.getInstance().timeInMillis % Integer.MAX_VALUE).toInt(),
        "",
        "",
        Calendar.getInstance().timeInMillis,
        0L,
        true,
        false
    )
}
