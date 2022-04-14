package com.rustamft.tasksft.ui.screens.backup

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rustamft.tasksft.R
import com.rustamft.tasksft.database.entity.Task
import com.rustamft.tasksft.database.prefs.SharedPrefs
import com.rustamft.tasksft.database.repository.Repo
import com.rustamft.tasksft.utils.TasksBackupManager
import com.rustamft.tasksft.utils.displayToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val prefs: SharedPrefs,
    private val repo: Repo<Task>
) : ViewModel() {

    private val listOfTasks = repo.getAll()
    lateinit var exportBackupLauncher: ActivityResultLauncher<Intent>
    lateinit var importBackupLauncher: ActivityResultLauncher<Intent>

    fun navigateBack(view: View) {
        val navController = view.findNavController()
        navController.popBackStack()
    }

    fun chooseExportDir(view: View) {
        val onChooseDir = {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            try {
                exportBackupLauncher.launch(intent)
            } catch (e: ActivityNotFoundException) {
                view.context.displayToast(e.message.toString())
            }
        }
        if (prefs.hasBackupDirPermission()) {
            displayExportConfirmation(view, prefs.getBackupDir()!!, onChooseDir)
        } else {
            onChooseDir()
        }
    }

    fun exportTasks(view: View, result: ActivityResult) {
        with(view.context) {
            val backupDirUri = result.data?.data
            if (backupDirUri == null) {
                displayToast(getString(R.string.could_not_do))
            } else {
                prefs.setBackupDir(backupDirUri)
                writeBackupToDisk(view, backupDirUri.toString())
            }
        }
    }

    fun chooseImportFile(view: View) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
        }
        try {
            importBackupLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            view.context.displayToast(e.message.toString())
        }
    }

    fun importTasks(view: View, result: ActivityResult) {
        with(view.context) {
            val backupFileUri = result.data?.data
            if (backupFileUri == null) {
                displayToast(getString(R.string.could_not_do))
            } else {
                readBackupFromDisk(view, backupFileUri)
            }
        }
    }

    private fun displayExportConfirmation(
        view: View,
        backupDir: String,
        onChooseDir: () -> Unit
    ) {
        with(view.context) {
            val builder = AlertDialog.Builder(this)
                .setTitle(R.string.action_export)
                .setMessage(getString(R.string.backup_will_be_saved_to) + backupDir)
                .setPositiveButton(R.string.action_save) { _, _ ->
                    writeBackupToDisk(view, backupDir)
                }
                .setNegativeButton(R.string.action_cancel) { _, _ ->
                    // Close.
                }
                .setNeutralButton(R.string.choose_dir) { _, _ ->
                    onChooseDir()
                }
            builder.show()
        }
    }

    private fun writeBackupToDisk(view: View, backupDir: String) {
        viewModelScope.launch {
            with(view.context) {
                val listOfTasks: List<Task>? = listOfTasks.firstOrNull()
                if (listOfTasks == null || listOfTasks.isEmpty()) {
                    displayToast(getString(R.string.could_not_do))
                    return@launch
                }
                val json = Gson().toJson(listOfTasks)
                val backupManager = TasksBackupManager(this)
                val isSuccess = backupManager.writeFileToDisk(backupDir, json)
                if (isSuccess) {
                    displayToast(getString(R.string.backup_file_exported))
                    navigateBack(view)
                }
            }
        }
    }

    private fun readBackupFromDisk(view: View, backupFileUri: Uri) {
        viewModelScope.launch {
            with(view.context) {
                var listOfTasks: List<Task>? = null
                val result = kotlin.runCatching {
                    val backupManager = TasksBackupManager(this)
                    val json = backupManager.readFileFromDisk(backupFileUri)
                    val type = object : TypeToken<List<Task>>() {}.type
                    listOfTasks = Gson().fromJson(json, type)
                }
                if (result.isSuccess && listOfTasks != null) {
                    repo.insert(listOfTasks!!)
                    displayToast(getString(R.string.backup_file_imported))
                    navigateBack(view)
                }
            }
        }
    }
}
