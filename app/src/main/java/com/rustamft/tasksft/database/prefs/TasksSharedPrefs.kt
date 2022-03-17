package com.rustamft.tasksft.database.prefs

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.rustamft.tasksft.utils.Constants.NIGHT_MODE
import com.rustamft.tasksft.utils.Constants.SHARED_PREFS
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TasksSharedPrefs @Inject constructor(
    @ApplicationContext private val context: Context
) : AppSharedPrefs {

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
}
