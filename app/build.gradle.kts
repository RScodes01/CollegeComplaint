plugins {
    id("com.android.application")
    id("com.google.gms.google-services") // For Firebase services
}

android {
    namespace = "com.example.collegecomplaint"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.collegecomplaint"
        minSdk = 24 // Ensure the minimum SDK supports features
        targetSdk = 34 // Target Android 13 (API 33) or higher to enable edge-to-edge
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Firebase BOM (Bill of Materials) for managing versions of Firebase libraries
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))

    // Firebase services
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore") // For Firestore database
    implementation("com.google.firebase:firebase-auth") // For Firebase Authentication

    // Android libraries
    implementation("androidx.appcompat:appcompat:1.6.1") // AppCompat library
    implementation("com.google.android.material:material:1.9.0") // Material Components
    implementation("androidx.activity:activity-ktx:1.6.1") // For activity extensions
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(libs.firebase.database) // For ConstraintLayout

    // Testing libraries
    testImplementation("junit:junit:4.13.2") // Unit tests
    androidTestImplementation("androidx.test.ext:junit:1.1.5") // Android JUnit tests
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1") // Espresso UI tests
}
