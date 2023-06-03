package com.rustamft.tasksft.data.storage.disk

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import androidx.documentfile.provider.DocumentFile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rustamft.tasksft.data.model.TaskData
import com.rustamft.tasksft.data.storage.BackupStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.InputStreamReader

internal class BackupDiskStorage(
    private val context: Context
) : BackupStorage {

    override suspend fun save(
        fileName: String,
        tasks: List<TaskData>,
        directoryUri: Uri
    ) {
        val file = createFile(fileName = fileName, directoryUri = directoryUri)
        val descriptor = context.contentResolver.openFileDescriptor(file!!.uri, "wt")!!
        val stream = FileOutputStream(descriptor.fileDescriptor)
        val json = Gson().toJson(tasks)
        withContext(Dispatchers.IO) {
            stream.write(json.toByteArray())
            stream.close()
        }
        descriptor.close()
    }

    override fun get(fileUri: Uri): Flow<List<TaskData>> {
        return flow {
            val list = emptyList<TaskData>().toMutableList()
            val json = readFile(fileUri)
            val type = object : TypeToken<List<TaskData>>() {}.type
            list.addAll(Gson().fromJson(json, type))
            emit(list)
        }
    }

    private fun createFile(fileName: String, directoryUri: Uri): DocumentFile? {
        // Get a valid parent URI
        val parentDocument = DocumentFile.fromTreeUri(context, directoryUri)!!
        val parentDocumentUri = parentDocument.uri
        // Create new file
        val fileUri = DocumentsContract.createDocument(
            context.contentResolver,
            parentDocumentUri,
            "",
            fileName
        )
        return DocumentFile.fromSingleUri(
            context,
            fileUri ?: return null
        )
    }

    private fun readFile(fileUri: Uri): String {
        val stringBuilder = StringBuilder()
        context.contentResolver.openInputStream(fileUri).use { inputStream ->
            if (inputStream == null) return@use
            BufferedReader(InputStreamReader(inputStream)).use { br ->
                var line = br.readLine()
                while (line != null) {
                    stringBuilder.append(line)
                    line = br.readLine()
                }
            }
        }
        return stringBuilder.toString()
    }
}
