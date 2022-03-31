package com.rustamft.tasksft.database.entity

import android.content.Context
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import com.rustamft.tasksft.utils.BACKUP_FILE_NAME
import com.rustamft.tasksft.utils.TAG_BACKUP_FILE
import com.rustamft.tasksft.utils.datetime.DateTimeProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileOutputStream

class BackupFile(
    private val context: Context,
    private val dir: String,
    private val json: String
) {

    suspend fun writeToDisk(): Boolean {
        val result = withContext(Dispatchers.IO) {
            kotlin.runCatching {
                val file = createFile() ?: return@runCatching
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

    private fun createFile(): DocumentFile? {
        // Get a valid parent URI.
        val parentDocument = DocumentFile.fromTreeUri(context, Uri.parse(dir))!!
        val parentDocumentUri = parentDocument.uri
        // Create new file.
        val fileUri = DocumentsContract.createDocument(
            context.contentResolver,
            parentDocumentUri,
            "",
            BACKUP_FILE_NAME + DateTimeProvider.getNowAsString()
        )
        return DocumentFile.fromSingleUri(
            context,
            fileUri ?: return null
        )
    }
}
