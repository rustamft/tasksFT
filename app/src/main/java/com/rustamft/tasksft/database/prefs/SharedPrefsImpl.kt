package com.rustamft.tasksft.database.prefs

import android.content.Context
import android.content.Intent
import android.content.UriPermission
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import com.rustamft.tasksft.utils.BACKUP_DIR
import com.rustamft.tasksft.utils.NIGHT_MODE
import com.rustamft.tasksft.utils.SHARED_PREFS
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPrefsImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SharedPrefs {

    private val prefs =
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

    override fun setNightMode(mode: Int) {
        prefs
            .edit()
            .putInt(NIGHT_MODE, mode)
            .apply()
    }

    override fun getNightMode(): Int {
        return prefs.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    override fun setBackupDir(uri: Uri) {
        // Persist the permission.
        val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        context.contentResolver.takePersistableUriPermission(uri, flags)
        // Save to SharedPrefs.
        val backupDir = uri.toString()
        prefs
            .edit()
            .putString(BACKUP_DIR, backupDir)
            .apply()
    }

    override fun getBackupDir(): String? = prefs.getString(BACKUP_DIR, null)

    override fun hasBackupDirPermission(): Boolean {
        val dir = getBackupDir() ?: return false
        val permissionsList: List<UriPermission> = context.contentResolver.persistedUriPermissions
        if (permissionsList.isNotEmpty()) {
            for (permission in permissionsList) {
                if (permission.uri.toString() == dir) {
                    if (permission.isWritePermission && permission.isReadPermission) {
                        return true
                    }
                }
            }
        }
        return false
    }
}
