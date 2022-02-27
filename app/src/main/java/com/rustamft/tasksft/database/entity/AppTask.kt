package com.rustamft.tasksft.database.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AppTask(
    @PrimaryKey var id: Int,
    var title: String,
    var description: String,
    @NonNull @ColumnInfo(name = "millis") var millis: Long,
    @NonNull @ColumnInfo(name = "is_finished") var isFinished: Boolean
) {

    constructor() : this(-1, "", "", 0L, false) // Empty task.

}
