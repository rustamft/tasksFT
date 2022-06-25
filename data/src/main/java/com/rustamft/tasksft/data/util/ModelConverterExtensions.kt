package com.rustamft.tasksft.data.util

import com.google.gson.Gson
import com.rustamft.tasksft.data.model.AppPreferencesData
import com.rustamft.tasksft.data.model.TaskData
import com.rustamft.tasksft.domain.model.AppPreferences
import com.rustamft.tasksft.domain.model.Task
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun AppPreferences.convert(): AppPreferencesData =
    typeConvert(AppPreferencesData::class.java)

internal fun AppPreferencesData.convert(): AppPreferences = typeConvert(AppPreferences::class.java)
internal fun Task.convert(): TaskData = typeConvert(TaskData::class.java)
internal fun TaskData.convert(): Task = typeConvert(Task::class.java)

internal suspend fun List<Task>.convert(): List<TaskData> {
    return coroutineScope {
        this@convert.map { task ->
            async { task.convert() }
        }.awaitAll()
    }
}

internal fun Flow<List<TaskData>>.convert(): Flow<List<Task>> {
    return this.map { listData ->
        coroutineScope {
            listData.map { taskData ->
                async {
                    taskData.convert()
                }
            }.awaitAll()
        }
    }
}

private fun <I, O> I.typeConvert(type: Class<O>): O {
    val gson = Gson()
    val json = gson.toJson(this)
    return gson.fromJson(json, type)
}
