package com.rustamft.tasksft.ui.screens.list

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.rustamft.tasksft.R
import com.rustamft.tasksft.database.entity.BackupFile
import com.rustamft.tasksft.database.entity.Task
import com.rustamft.tasksft.database.prefs.SharedPrefs
import com.rustamft.tasksft.database.repository.TasksRepo
import com.rustamft.tasksft.ui.displayToast
import com.rustamft.tasksft.utils.GITHUB_LINK
import com.rustamft.tasksft.utils.TASK_ID
import com.rustamft.tasksft.utils.datetime.DateTimeProvider
import com.rustamft.tasksft.utils.schedule.TasksWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val prefs: SharedPrefs,
    private val repo: TasksRepo,
    private val workManager: TasksWorkManager
) : ViewModel() {

    val listOfTasks = repo.getTasksList()

    fun navigateNext(view: View) {
        val navController = view.findNavController()
        navController.navigate(R.id.action_listFragment_to_editorFragment)
    }

    fun navigateNext(view: View, id: Int): Boolean {
        val bundle = Bundle()
        bundle.putInt(TASK_ID, id)
        val navController = view.findNavController()
        navController.navigate(R.id.action_listFragment_to_editorFragment, bundle)
        return true
    }

    fun getNightMode(): Int = prefs.getNightMode()

    fun switchNightMode() {
        val mode: Int = when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
                AppCompatDelegate.MODE_NIGHT_YES
            }
            AppCompatDelegate.MODE_NIGHT_YES -> {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            else -> {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        }
        prefs.setNightMode(mode)
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    fun updateNightModeMenuIcon(context: Context, item: MenuItem) {
        val drawableId: Int = when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
                R.drawable.ic_night_mode_auto
            }
            AppCompatDelegate.MODE_NIGHT_YES -> {
                R.drawable.ic_night_mode_on
            }
            else -> {
                R.drawable.ic_night_mode_off
            }
        }
        val drawable = ContextCompat.getDrawable(context, drawableId)
        if (item.icon != drawable) {
            item.icon = drawable
        }
    }

    fun defineDateTimeText(task: Task): String {
        return if (task.reminder == 0L) { // If there is no reminder.
            "" // Empty string.
        } else { // If there is a reminder time inside the task.
            DateTimeProvider.millisToDateTimeString(task.reminder).string
        }
    }

    fun onTaskChecked(task: Task, checked: ObservableBoolean) {
        task.isFinished = !task.isFinished
        checked.set(!checked.get())
        updateTask(task)
    }

    fun deleteFinishedTasks() {
        viewModelScope.launch {
            val list = repo.getFinishedTasks()
            launch {
                repo.delete(list)
            }
            launch {
                workManager.cancel(list)
            }
        }
    }

    fun exportTasks(context: Context, onChooseDir: () -> Unit) {
        if (prefs.hasBackupDirPermission()) {
            displayBackupConfirmation(context, prefs.getBackupDir()!!, onChooseDir)
        } else {
            onChooseDir()
        }
    }

    fun exportTasks(context: Context, backupDirUri: Uri) {
        prefs.setBackupDir(backupDirUri)
        writeBackupToDisk(context, backupDirUri.toString())
    }

    fun displayAboutApp(context: Context) {
        val message = context.getString(R.string.about_app_content) + getAppVersion(context)
        val builder = AlertDialog.Builder(context)
            .setTitle(R.string.about_app)
            .setMessage(message)
            .setPositiveButton(R.string.action_close) { _, _ ->
                // Close.
            }
            .setNegativeButton("GitHub") { _, _ ->
                openGitHub(context)
            }
        builder.show()
    }

    private fun displayBackupConfirmation(
        context: Context,
        backupDir: String,
        onChooseDir: () -> Unit
    ) {
        val builder = AlertDialog.Builder(context)
            .setTitle(R.string.action_export)
            .setMessage(context.getString(R.string.backup_will_be_saved_to) + backupDir)
            .setPositiveButton(R.string.action_save) { _, _ ->
                writeBackupToDisk(context, backupDir)
            }
            .setNegativeButton(R.string.action_cancel) { _, _ ->
                // Close.
            }
            .setNeutralButton(R.string.choose_dir) { _, _ ->
                onChooseDir()
            }
        builder.show()
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch {
            launch {
                repo.update(task)
            }
            workManager.cancel(task)
            if (!task.isFinished && DateTimeProvider.isInFuture(task.reminder)) {
                workManager.scheduleOneTime(task)
            }
        }
    }

    private fun writeBackupToDisk(context: Context, backupDir: String) {
        viewModelScope.launch {
            with(context) {
                val listOfTasks = listOfTasks.firstOrNull() ?: {
                    displayToast(getString(R.string.could_not_do))
                }
                val json = Gson().toJson(listOfTasks)
                if (BackupFile(this, backupDir, json).writeToDisk()) {
                    displayToast(getString(R.string.backup_file_created))
                }
            }
        }
    }

    private fun getAppVersion(context: Context): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            "Not available"
        }
    }

    private fun openGitHub(context: Context) {
        val webPage = Uri.parse(GITHUB_LINK)
        val intent = Intent(Intent.ACTION_VIEW, webPage)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent, null)
        }
    }
}
