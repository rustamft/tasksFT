package com.rustamft.tasksft.data.storage.disk

import android.content.Context
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import androidx.documentfile.provider.DocumentFile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rustamft.tasksft.data.model.TaskData
import com.rustamft.tasksft.data.storage.BackupStorage
import com.rustamft.tasksft.domain.util.BACKUP_FILE_EXTENSION
import com.rustamft.tasksft.domain.util.BACKUP_FILE_NAME
import com.rustamft.tasksft.domain.util.format
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.util.Calendar

internal class BackupDiskStorage(
    private val context: Context
) : BackupStorage {

    override suspend fun save(directoryUri: Uri, list: List<TaskData>) {
        kotlin.runCatching {
            val json = Gson().toJson(list)
            val file = createFile(directoryUri)
            val pfd: ParcelFileDescriptor =
                context.contentResolver.openFileDescriptor(file!!.uri, "wt")!!
            val fos = FileOutputStream(pfd.fileDescriptor)
            fos.write(json.toByteArray())
            fos.close()
            pfd.close()
        }
    }

    override fun get(fileUri: Uri): Flow<List<TaskData>> {
        return flow {
            val list = emptyList<TaskData>().toMutableList()
            kotlin.runCatching {
                val json = readFile(fileUri)
                val type = object : TypeToken<List<TaskData>>() {}.type
                list.addAll(Gson().fromJson(json, type))
            }
            emit(list)
        }
    }

    private fun createFile(directoryUri: Uri): DocumentFile? {
        // Get a valid parent URI
        val parentDocument = DocumentFile.fromTreeUri(context, directoryUri)!!
        val parentDocumentUri = parentDocument.uri
        // Create new file
        val currentTimeString = with(Calendar.getInstance()) {
            "${
                get(Calendar.YEAR)
            }-${
                get(Calendar.MONTH).format(2)
            }-${
                get(Calendar.DAY_OF_MONTH).format(2)
            }-${
                get(Calendar.HOUR_OF_DAY).format(2)
            }-${
                get(Calendar.MINUTE).format(2)
            }-${
                get(Calendar.SECOND).format(2)
            }"
        }
        val fileName = BACKUP_FILE_NAME + currentTimeString + BACKUP_FILE_EXTENSION
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
        kotlin.runCatching {
            context.contentResolver.openInputStream(fileUri).use { inputStream ->
                BufferedReader(InputStreamReader(inputStream!!)).use { br ->
                    var line: String
                    while (br.readLine().also { line = it } != null) {
                        stringBuilder.append(line)
                    }
                }
            }
        }
        return stringBuilder.toString()
    }
}
