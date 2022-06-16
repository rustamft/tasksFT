package com.rustamft.tasksft.data.util

import com.google.gson.Gson
import com.rustamft.tasksft.data.model.AppPreferencesData
import com.rustamft.tasksft.data.model.TaskData
import com.rustamft.tasksft.domain.model.AppPreferences
import com.rustamft.tasksft.domain.model.Task

internal fun AppPreferences.convert(): AppPreferencesData =
    typeConvert(AppPreferencesData::class.java)

internal fun AppPreferencesData.convert(): AppPreferences = typeConvert(AppPreferences::class.java)
internal fun Task.convert(): TaskData = typeConvert(TaskData::class.java)
internal fun TaskData.convert(): Task = typeConvert(Task::class.java)
//internal fun <I, O> List<I>.convert(): List<O> = listTypeConvert() // TODO: remove

private fun <I, O> I.typeConvert(type: Class<O>): O {
    val gson = Gson()
    val json = gson.toJson(this)
    //val type = object : TypeToken<I>() {}.type // TODO: remove
    return gson.fromJson(json, type)
}
