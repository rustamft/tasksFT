plugins {
    id("com.android.application") version "7.3.0" apply false
    id("com.android.library") version "7.3.0" apply false
    id("org.jetbrains.kotlin.android") version Constants.KOTLIN_VERSION apply false
    id("org.jetbrains.kotlin.jvm") version Constants.KOTLIN_VERSION apply false
}

task<Delete>("clean") {
    delete = setOf(rootProject.buildDir)
}
