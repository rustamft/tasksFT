plugins {
    id("com.android.application") version "8.0.2" apply false
    id("com.android.library") version "8.0.2" apply false
    id("org.jetbrains.kotlin.android") version Constants.KOTLIN_VERSION apply false
    id("org.jetbrains.kotlin.jvm") version Constants.KOTLIN_VERSION apply false
}

task<Delete>("clean") {
    delete = setOf(rootProject.buildDir)
}
