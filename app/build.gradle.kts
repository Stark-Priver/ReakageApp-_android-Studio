plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose) // Ensure this is correct
}

repositories {
    google()
    mavenCentral()
}

android {
    namespace = "com.example.reakageapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.reakageapp"
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom)) // BOM for Compose
    implementation(libs.androidx.compose.ui) // Compose UI
    implementation(libs.androidx.compose.ui.graphics) // Compose UI Graphics
    implementation(libs.androidx.compose.ui.tooling.preview) // Compose Preview
    implementation(libs.androidx.material3) // Material3
    implementation(libs.androidx.navigation.runtime.ktx) // Navigation
    implementation(libs.androidx.navigation.compose) // Navigation Compose
    implementation("io.coil-kt:coil-compose:2.0.0") // Coil for image loading
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom)) // BOM for testing
    androidTestImplementation(libs.androidx.compose.ui.test.junit4) // Compose UI Testing
    debugImplementation(libs.androidx.compose.ui.tooling) // Debugging tools
    debugImplementation(libs.androidx.compose.ui.test.manifest) // Manifest for testing
}
