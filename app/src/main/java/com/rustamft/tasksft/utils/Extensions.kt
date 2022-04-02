package com.rustamft.tasksft.utils

import android.content.Context
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner

fun FragmentActivity.enableBackCallback(owner: LifecycleOwner, onBackClicked: () -> Unit) {
    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onBackClicked()
        }
    }
    this.onBackPressedDispatcher.addCallback(owner, callback)
}

fun Context.displayToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
