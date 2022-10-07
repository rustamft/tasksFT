plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    namespace = "com.rustamft.data"
}

dependencies {

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    implementation(project(":domain"))
    // DocumentFile
    implementation("androidx.documentfile:documentfile:1.0.1")
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Constants.COROUTINE_VERSION}")
    // DataStore
    implementation("androidx.datastore:datastore:1.0.0")
    // Room
    val roomVersion = "2.5.0-beta01"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    // Gson converter
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Koin
    implementation("io.insert-koin:koin-android:${Constants.KOIN_VERSION}")
    testImplementation("io.insert-koin:koin-test:${Constants.KOIN_VERSION}")
    testImplementation("io.insert-koin:koin-test-junit4:${Constants.KOIN_VERSION}")
}
