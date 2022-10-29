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
        versionName = "1.0.2"

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

    // Modules
    implementation(project(":domain"))
    implementation(project(":data"))
    // Kotlin extensions for 'core' artifact
    implementation("androidx.core:core-ktx:1.9.0")
    // Lifecycle
    val lifecycleVersion = "2.5.1"
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-service:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
    // Activity
    implementation("androidx.activity:activity-compose:1.6.0")
    // Compose
    val composeVersion = "1.3.0-rc01"
    implementation("androidx.compose.ui:ui:1.3.0")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")
    // Accompanist
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.27.0")
    // Compose destinations
    val composeDestinationsVersion = "1.7.23-beta"
    implementation("io.github.raamcosta.compose-destinations:core:$composeDestinationsVersion")
    ksp("io.github.raamcosta.compose-destinations:ksp:$composeDestinationsVersion")
    // Koin
    val koinVersion = "3.3.0"
    val koinTestVersion = "3.2.2"
    implementation("io.insert-koin:koin-android:$koinVersion")
    implementation("io.insert-koin:koin-androidx-compose:$koinVersion")
    testImplementation("io.insert-koin:koin-test:$koinTestVersion")
    testImplementation("io.insert-koin:koin-test-junit4:$koinTestVersion")
    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.7.1")
    // Coroutines test
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    // LeakCanary
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.9.1")
    // JUnit
    val junitVersion = "5.9.1"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    // MockK
    testImplementation("io.mockk:mockk:1.13.2")
    // Kaspresso
    val kaspressoVersion = "1.4.2"
    androidTestImplementation("com.kaspersky.android-components:kaspresso:$kaspressoVersion")
    androidTestImplementation("com.kaspersky.android-components:kaspresso-compose-support:$kaspressoVersion")
}
