plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.carrental.speedmonitor"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.carrental.speedmonitor"
        minSdk = 28
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
        compose = true
    }

    useLibrary ("android.car")

//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.5.14"
//    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.activity.compose.v1101)
    implementation(libs.androidx.ui.v165)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.tooling.preview.v165)
    debugImplementation(libs.androidx.ui.tooling)


    // Lifecycle
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    // Firebase Functions (Optional)

    implementation(platform("com.google.firebase:firebase-bom:34.0.0"))

    implementation("com.google.firebase:firebase-database-ktx:20.2.0")
    implementation("com.google.firebase:firebase-firestore-ktx:24.7.0")

//    implementation("androidx.car.app:app:1.7.0")
}
