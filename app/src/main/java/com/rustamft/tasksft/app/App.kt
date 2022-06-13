package com.rustamft.tasksft.app

import android.app.Application
import com.rustamft.tasksft.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale

@HiltAndroidApp
class App : Application() {

    companion object App {
        val language: String get() = Locale.getDefault().language // TODO: delete if not needed
        const val version = BuildConfig.VERSION_NAME
    }
}
