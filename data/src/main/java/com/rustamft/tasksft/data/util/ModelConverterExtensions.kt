package com.rustamft.tasksft.data.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rustamft.tasksft.data.model.AppPreferencesData
import com.rustamft.tasksft.data.model.TaskData
import com.rustamft.tasksft.domain.model.AppPreferences
import com.rustamft.tasksft.domain.model.Task

internal fun AppPreferences.convert(): AppPreferencesData = typeConvert()
internal fun AppPreferencesData.convert(): AppPreferences = typeConvert()
internal fun Task.convert(): TaskData = typeConvert()
internal fun TaskData.convert(): Task = typeConvert()

private fun <I, O> I.typeConvert(): O {
    val gson = Gson()
    val json = gson.toJson(this)
    val type = object : TypeToken<I>() {}.type
    return gson.fromJson(json, type)
}
