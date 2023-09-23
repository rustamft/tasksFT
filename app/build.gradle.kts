plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp") version "${Constants.KOTLIN_VERSION}-1.0.13"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
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
    compileSdk = 34
    defaultConfig {
        applicationId = "com.rustamft.tasksft"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.8"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    namespace = "com.rustamft.tasksft"
}

dependencies {

    // Modules
    implementation(project(":domain"))
    implementation(project(":data"))
    // Kotlin extensions for 'core' artifact
    implementation("androidx.core:core-ktx:1.12.0")
    // Lifecycle
    val lifecycleVersion = "2.6.2"
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-service:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
    // Activity
    implementation("androidx.activity:activity-compose:1.7.2")
    // Compose
    val composeVersion = "1.5.1"
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")
    // Accompanist
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.28.0")
    // Compose destinations
    val composeDestinationsVersion = "1.9.53"
    implementation("io.github.raamcosta.compose-destinations:core:$composeDestinationsVersion")
    ksp("io.github.raamcosta.compose-destinations:ksp:$composeDestinationsVersion")
    // Koin
    implementation("io.insert-koin:koin-android:${Constants.KOIN_VERSION}")
    implementation("io.insert-koin:koin-androidx-compose:${Constants.KOIN_VERSION}")
    testImplementation("io.insert-koin:koin-test:${Constants.KOIN_VERSION}")
    testImplementation("io.insert-koin:koin-test-junit4:${Constants.KOIN_VERSION}")
    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    // Coroutines test
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Constants.COROUTINES_VERSION}")
    // LeakCanary
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.12")
    // JUnit
    val junitVersion = "5.10.0"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    // MockK
    testImplementation("io.mockk:mockk:1.13.8")
    // Kaspresso
    val kaspressoVersion = "1.5.3"
    androidTestImplementation("com.kaspersky.android-components:kaspresso:$kaspressoVersion")
    androidTestImplementation("com.kaspersky.android-components:kaspresso-compose-support:$kaspressoVersion")
}
