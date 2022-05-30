package com.rustamft.tasksft.data.model

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.InputStream
import java.io.OutputStream

internal data class DataContainer(
    val appPreferencesData: AppPreferencesData = AppPreferencesData(),
    val taskData: TaskData = TaskData()
) {

    companion object Serializer : androidx.datastore.core.Serializer<DataContainer> {

        private val gson = Gson()

        override val defaultValue: DataContainer
            get() = DataContainer()

        override suspend fun readFrom(input: InputStream): DataContainer {
            return try {
                val json = input.readBytes().decodeToString()
                gson.fromJson(json, DataContainer::class.java)
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
                defaultValue
            }
        }

        override suspend fun writeTo(t: DataContainer, output: OutputStream) {
            kotlin.runCatching {
                output.write(gson.toJson(t).encodeToByteArray())
            }
        }
    }
}
