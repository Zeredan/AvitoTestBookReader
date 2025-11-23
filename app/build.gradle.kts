import com.android.build.api.dsl.Packaging

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics.gradle)

    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "test.task.avitotestbookreader"
    compileSdk = 35

    defaultConfig {
        applicationId = "test.task.avitotestbookreader"
        minSdk = 24
        targetSdk = 35
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
        compose = true
    }
}

dependencies {
    implementation(project(":core:ui"))

    implementation(project(":feature"))
    implementation(project(":feature:splash"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:books"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:uploader"))
    implementation(project(":feature:reader"))

    implementation(project(":domain:settings"))
    implementation(project(":domain:auth"))

    implementation(project(":data:settings:impl"))
    implementation(project(":data:books:impl"))
    implementation(project(":data:auth:impl"))

//    val flavorName = project.gradle.startParameter.taskNames.joinToString().let {
//        when {
//            it.contains("Mock", ignoreCase = true) -> "mock"
//            it.contains("Real", ignoreCase = true) -> "real"
//            else -> "real" // По умолчанию реальный
//        }
//    }
//    when (flavorName) {
//        "mock" -> implementation(project(":data:courses:mock"))
//        "real" -> implementation(project(":data:courses:impl"))
//    }

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.firebase.messaging)
    implementation(libs.firebase.crashlytics)
    androidTestImplementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)


    implementation(platform(libs.firebase.bom))

    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.config)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.appcheck.debug)
    implementation(libs.firebase.appcheck.playintegrity)

}