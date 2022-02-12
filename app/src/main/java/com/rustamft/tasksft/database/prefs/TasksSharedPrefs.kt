package com.rustamft.tasksft.database.prefs

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TasksSharedPrefs @Inject constructor(
    @ApplicationContext private val context: Context
) : AppSharedPrefs {

    private val prefs =
        context.getSharedPreferences("com.rustamft.tasksFT.shared_prefs", Context.MODE_PRIVATE)
    private val nightMode = "night_mode"

    // TODO: Add night mode changing func.
    override fun setNightMode(mode: Int) {
        prefs
            .edit()
            .putInt(nightMode, mode)
            .apply()
    }

    override fun getNightMode(): Int {
        return prefs.getInt(nightMode, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}