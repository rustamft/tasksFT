package com.rustamft.tasksft.data.model

import com.google.gson.Gson
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.domain.model.Task
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.supervisorScope

internal fun Preferences.map(): PreferencesData {
    return PreferencesData(
        theme = this.theme,
        backupDirectory = this.backupDirectory
    )
}

internal fun PreferencesData.map(): Preferences {
    return Preferences(
        theme = this.theme,
        backupDirectory = this.backupDirectory
    )
}

internal fun Task.map(): TaskData = this.map(TaskData::class.java)

internal fun TaskData.map(): Task = this.map(Task::class.java)

internal suspend fun List<Task>.map(): List<TaskData> = this.map(TaskData::class.java)

internal fun Flow<List<TaskData>>.map(): Flow<List<Task>> {
    return this.map(Task::class.java)
}

private fun <I, O> Flow<List<I>>.map(outputType: Class<O>): Flow<List<O>> {
    return this.map { value ->
        supervisorScope {
            value.map { element ->
                async {
                    element.map(outputType)
                }
            }.awaitAll()
        }
    }
}

private suspend fun <I, O> List<I>.map(outputType: Class<O>): List<O> {
    return coroutineScope {
        this@map.map { element ->
            async { element.map(outputType) }
        }.awaitAll()
    }
}

private fun <I, O> I.map(outputType: Class<O>): O {
    val gson = Gson()
    val json = gson.toJson(this)
    return gson.fromJson(json, outputType)
}
