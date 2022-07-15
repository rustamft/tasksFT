package com.rustamft.tasksft.data.model

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.InputStream
import java.io.OutputStream

internal data class AppPreferencesData(
    val darkTheme: Boolean? = null,
    val backupDirectory: String = ""
) {

    companion object Serializer : androidx.datastore.core.Serializer<AppPreferencesData> {

        private val gson = Gson()

        override val defaultValue: AppPreferencesData
            get() = AppPreferencesData()

        override suspend fun readFrom(input: InputStream): AppPreferencesData {
            return try {
                val json = input.readBytes().decodeToString()
                gson.fromJson(json, AppPreferencesData::class.java)
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
                defaultValue
            }
        }

        override suspend fun writeTo(t: AppPreferencesData, output: OutputStream) {
            kotlin.runCatching {
                output.write(gson.toJson(t).encodeToByteArray())
            }
        }
    }
}
