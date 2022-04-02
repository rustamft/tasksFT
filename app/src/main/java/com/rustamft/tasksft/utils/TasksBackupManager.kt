package com.rustamft.tasksft.utils

import android.content.Context
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import com.rustamft.tasksft.utils.datetime.DateTimeProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.InputStreamReader

class TasksBackupManager(private val context: Context) {

    suspend fun writeFileToDisk(dir: String, json: String): Boolean {
        val result = withContext(Dispatchers.IO) {
            kotlin.runCatching {
                val file = createFile(dir) ?: return@runCatching
                // Write the text to the file.
                val pfd: ParcelFileDescriptor =
                    context.contentResolver.openFileDescriptor(file.uri, "wt")!!
                val fos = FileOutputStream(pfd.fileDescriptor)
                fos.write(json.toByteArray())
                fos.close()
                pfd.close()
            }
        }
        Log.d(TAG_BACKUP_FILE, result.exceptionOrNull().toString())
        return result.isSuccess
    }

    suspend fun readFileFromDisk(fileUri: Uri): String {
        val stringBuilder = StringBuilder()
        val result = withContext(Dispatchers.IO) {
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
        }
        Log.d(TAG_BACKUP_FILE, result.exceptionOrNull().toString())
        return stringBuilder.toString()
    }

    private fun createFile(dir: String): DocumentFile? {
        // Get a valid parent URI.
        val parentDocument = DocumentFile.fromTreeUri(context, Uri.parse(dir))!!
        val parentDocumentUri = parentDocument.uri
        // Create new file.
        val fileName =
            BACKUP_FILE_NAME + DateTimeProvider.getNowAsString() + BACKUP_FILE_EXTENSION
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
}
