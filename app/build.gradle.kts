plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    kotlin("kapt")
}

android {
    namespace = "com.example.projektr"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.projektr"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
    packaging {
        resources {
            excludes += setOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",
                "META-INF/DEPENDENCIES",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/LICENSE.txt"
            )
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
    implementation(libs.firebase.firestore)
    implementation(libs.androidx.espresso.intents)
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.espresso.contrib) {
        exclude(
            group = "com.google.protobuf",
            module = "protobuf-lite"
        )
    }

    kapt(libs.androidx.room.compiler)

    // unit testovi
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("org.mockito:mockito-core:5.5.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")

    // funkcijski testovi
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.espresso.intents)
    androidTestImplementation(libs.junit.jupiter)
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("com.google.firebase:firebase-auth")
    androidTestImplementation("org.mockito:mockito-android:5.5.0")
    androidTestImplementation("androidx.fragment:fragment-testing:1.6.2")


    // Java language implementation
    implementation(libs.androidx.fragment)
    // Kotlin
    implementation(libs.androidx.fragment.ktx)
}