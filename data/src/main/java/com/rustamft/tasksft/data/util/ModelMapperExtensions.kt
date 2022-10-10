package com.rustamft.tasksft.data.util

import com.google.gson.Gson
import com.rustamft.tasksft.data.model.PreferencesData
import com.rustamft.tasksft.data.model.TaskData
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.domain.model.Task
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.supervisorScope

internal fun Preferences.mapToData(): PreferencesData {
    return PreferencesData(
        theme = this.theme,
        backupDirectory = this.backupDirectory
    )
}

internal fun PreferencesData.mapFromData(): Preferences {
    return Preferences(
        theme = this.theme,
        backupDirectory = this.backupDirectory
    )
}

internal fun Task.mapToData(): TaskData = this.mapTo(TaskData::class.java)

internal fun TaskData.mapFromData(): Task = this.mapTo(Task::class.java)

internal suspend fun List<Task>.mapToData(): List<TaskData> = this.mapToListOf(TaskData::class.java)

internal fun Flow<List<TaskData>>.mapFromData(): Flow<List<Task>> {
    return this.mapToFlowOfListOf(Task::class.java)
}

private fun <I, O> Flow<List<I>>.mapToFlowOfListOf(outputType: Class<O>): Flow<List<O>> {
    return this.map { value ->
        supervisorScope {
            value.map { element ->
                async {
                    element.mapTo(outputType)
                }
            }.awaitAll()
        }
    }
}

private suspend fun <I, O> List<I>.mapToListOf(outputType: Class<O>): List<O> {
    return coroutineScope {
        this@mapToListOf.map { element ->
            async { element.mapTo(outputType) }
        }.awaitAll()
    }
}

private fun <I, O> I.mapTo(outputType: Class<O>): O {
    val gson = Gson()
    val json = gson.toJson(this)
    return gson.fromJson(json, outputType)
}
