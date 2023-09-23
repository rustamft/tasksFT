plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "${Constants.KOTLIN_VERSION}-1.0.13"
}

android {
    compileSdk = 34
    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    namespace = "com.rustamft.data"
}

dependencies {

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")

    implementation(project(":domain"))
    // DocumentFile
    implementation("androidx.documentfile:documentfile:1.0.1")
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Constants.COROUTINES_VERSION}")
    // Room
    val roomVersion = "2.6.0-rc01"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    // Gson converter
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Koin
    implementation("io.insert-koin:koin-android:${Constants.KOIN_VERSION}")
    testImplementation("io.insert-koin:koin-test:${Constants.KOIN_VERSION}")
    testImplementation("io.insert-koin:koin-test-junit4:${Constants.KOIN_VERSION}")
}
