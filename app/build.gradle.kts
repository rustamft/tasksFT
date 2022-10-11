plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp") version "${Constants.KOTLIN_VERSION}-1.0.6"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
}

kotlin {
    sourceSets {
        debug {
            kotlin.srcDir("build/generated/ksp/debug/kotlin")
        }
        release {
            kotlin.srcDir("build/generated/ksp/release/kotlin")
        }
    }
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.rustamft.tasksft"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "0.9.13"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0-rc02"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    namespace = "com.rustamft.tasksft"
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    implementation(project(":domain"))
    implementation(project(":data"))
    // Lifecycle
    val lifecycleVersion = "2.5.1"
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-service:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
    // Activity
    implementation("androidx.activity:activity-compose:1.6.0")
    // Compose
    val composeVersion = "1.3.0-rc01"
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")
    // Accompanist
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.26.5-rc")
    // Compose destinations
    val composeDestinationsVersion = "1.7.22-beta"
    implementation("io.github.raamcosta.compose-destinations:core:$composeDestinationsVersion")
    ksp("io.github.raamcosta.compose-destinations:ksp:$composeDestinationsVersion")
    // Koin
    implementation("io.insert-koin:koin-android:${Constants.KOIN_VERSION}")
    implementation("io.insert-koin:koin-androidx-compose:${Constants.KOIN_VERSION}")
    testImplementation("io.insert-koin:koin-test:${Constants.KOIN_VERSION}")
    testImplementation("io.insert-koin:koin-test-junit4:${Constants.KOIN_VERSION}")
    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.7.1")
    // Coroutine test
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Constants.COROUTINE_VERSION}")
    // MockK
    testImplementation("io.mockk:mockk:1.13.2")
    // LeakCanary
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.9.1")
}
