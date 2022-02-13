package com.rustamft.tasksft.screens.list

import android.app.Application
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustamft.tasksft.database.entity.AppTask
import com.rustamft.tasksft.database.prefs.AppSharedPrefs
import com.rustamft.tasksft.database.repository.AppRepo
import com.rustamft.tasksft.utils.datetime.DateTimeUtil
import com.rustamft.tasksft.utils.schedule.AppWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val application: Application,
    private val prefs: AppSharedPrefs,
    private val repo: AppRepo,
    private val workManager: AppWorkManager
) : ViewModel() {

    fun setNightMode(mode: Int)  = prefs.setNightMode(mode)

    fun getNightMode(): Int = prefs.getNightMode()

    fun getList() = repo.getList()

    fun update(task: AppTask) {
        viewModelScope.launch {
            repo.update(task)
            if (task.isFinished) {
                workManager.cancel(task)
            } else if (DateTimeUtil.isInFuture(task.millis)) {
                workManager.scheduleOneTime(task)
            }
        }
    }

    fun deleteFinished() {
        viewModelScope.launch {
            val list = repo.getFinished()
            repo.delete(list)
            workManager.cancel(list)
        }
    }

    fun defineDateTimeText(task: AppTask): String {
        return if (task.millis == 0L) { // If there is no reminder.
            ""
        } else { // If there is a reminder time inside the task.
            DateTimeUtil.millisToString(task.millis).string
        }
    }

    fun buildAppVersion(): String {
        var appVersion =  "Not available"
        try {
            val packageInfo = application.packageManager.getPackageInfo(application.packageName, 0)
            appVersion = packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return appVersion
    }
}