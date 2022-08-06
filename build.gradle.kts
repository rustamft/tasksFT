/*
buildscript {
    extra.apply {
        set("kotlinVersion", "1.7.0")
        set("koinVersion", "3.2.0")
        set("coroutineVersion", "1.6.4")
    }
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
*/
plugins {
    id("com.android.application") version "7.2.2" apply false
    id("com.android.library") version "7.2.2" apply false
    id("org.jetbrains.kotlin.android") version Constants.KOTLIN_VERSION apply false
    id("org.jetbrains.kotlin.jvm") version Constants.KOTLIN_VERSION apply false
}

task<Delete>("clean") {
    delete = setOf(rootProject.buildDir)
}
