package com.rustamft.tasksft.screens.list

import android.app.Application
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.rustamft.tasksft.R
import com.rustamft.tasksft.database.entity.AppTask
import com.rustamft.tasksft.database.prefs.AppSharedPrefs
import com.rustamft.tasksft.database.repository.AppRepo
import com.rustamft.tasksft.utils.Constants
import com.rustamft.tasksft.utils.datetime.DateTimeUtil
import com.rustamft.tasksft.utils.schedule.AppWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val app: Application,
    private val prefs: AppSharedPrefs,
    private val repo: AppRepo,
    private val workManager: AppWorkManager
) : ViewModel() {

    val list = repo.getList()
    private val appVersion by lazy {
        var appVersion = "Not available"
        try {
            val packageInfo = app.packageManager.getPackageInfo(app.packageName, 0)
            appVersion = packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        appVersion
    }


    fun navigateNext(view: View) {
        val navController = view.findNavController()
        navController.navigate(R.id.action_listFragment_to_editorFragment)
    }

    fun navigateNext(view: View, id: Int): Boolean {
        val bundle = Bundle()
        bundle.putInt(Constants.TASK_ID, id)
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
        AppCompatDelegate.setDefaultNightMode(mode)
        prefs.setNightMode(mode)
    }

    fun updateNightModeMenuIcon(item: MenuItem) {
        val drawableID: Int = when (AppCompatDelegate.getDefaultNightMode()) {
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
        val drawable = ContextCompat.getDrawable(app, drawableID)
        if (item.icon != drawable) {
            item.icon = drawable
        }
    }

    fun deleteFinished() {
        viewModelScope.launch {
            val list = repo.getFinished()
            launch {
                repo.delete(list)
            }
            launch {
                workManager.cancel(list)
            }
        }
    }

    fun onTaskChecked(task: AppTask) {
        task.isFinished = !task.isFinished
        update(task)
    }

    fun defineDateTimeText(task: AppTask): String {
        return if (task.millis == 0L) { // If there is no reminder.
            "" // Empty string.
        } else { // If there is a reminder time inside the task.
            DateTimeUtil.millisToString(task.millis).string
        }
    }

    fun displayAboutApp(context: Context) {
        val message = context.getString(R.string.about_app_content) + appVersion
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


    private fun update(task: AppTask) {
        viewModelScope.launch {
            launch {
                repo.update(task)
            }
            workManager.cancel(task)
            if (!task.isFinished && DateTimeUtil.isInFuture(task.millis)) {
                workManager.scheduleOneTime(task)
            }
        }
    }

    private fun openGitHub(context: Context) {
        val webPage = Uri.parse(Constants.GITHUB_LINK)
        val intent = Intent(Intent.ACTION_VIEW, webPage)
        if (intent.resolveActivity(app.packageManager) != null) {
            context.startActivity(intent, null)
        }
    }
}
