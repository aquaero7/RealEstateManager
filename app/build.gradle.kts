@file:Suppress("UnstableApiUsage")

import org.gradle.internal.classpath.Instrumented.systemProperty

// For testOptions (Robolectric)

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Room: To use Kotlin Symbol Processing (KSP)
    id("com.google.devtools.ksp")
    // secrets-gradle-plugin
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.aquaero.realestatemanager"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.aquaero.realestatemanager"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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

            /* To keep keys and secrets hidden, using resValue (without secrets-gradle-plugin)
            resValue("string", "MAPS_API_KEY",
                "\"${project.findProperty("MAPS_API_KEY")}\""
            )
            */
            /* To keep keys and secrets hidden, using buildConfig (without secrets-gradle-plugin)
            buildConfigField("String", "MAPS_API_KEY",
                "\"${project.findProperty("MAPS_API_KEY")}\""
            )
            */
        }
        debug {
            /* To keep keys and secrets hidden, using resValue (without secrets-gradle-plugin)
            resValue("string", "MAPS_API_KEY",
                "\"${project.findProperty("MAPS_API_KEY")}\""
            )
            */
            /* To keep keys and secrets hidden, using buildConfig (without secrets-gradle-plugin)
            buildConfigField("String", "MAPS_API_KEY",
                "\"${project.findProperty("MAPS_API_KEY")}\""
            )
            */
        }
    }
    compileOptions {
        // sourceCompatibility = JavaVersion.VERSION_1_8
        // targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        // jvmTarget = "1.8"
        jvmTarget = "11"    //"19"
    }
    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true  // Added to allow BuildConfig import in Compose
    }
    composeOptions {
        // kotlinCompilerExtensionVersion = "1.4.3"
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    // For Robolectric
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    buildToolsVersion = "34.0.0"
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    // For PickVisualMedia contract
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.02.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    // Using icons extension, make sure to use R8 / ProGuard to remove unused icons from this application
    implementation("androidx.compose.material:material-icons-extended:1.6.2")
//    implementation("androidx.compose.material3:material3")    // Redundant with implementation for DatePickerColors ?
    implementation("androidx.compose.material3:material3-window-size-class:1.2.0")
    // DatePickerColors with complete customized colors
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation("androidx.compose.material3:material3-adaptive:1.0.0-alpha06")
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.0.0-alpha03")  // Updating beyond alpha3 version may cause errors
    //
    implementation("androidx.navigation:navigation-compose:2.7.7")
    // Accompanist-Permissions
    implementation("com.google.accompanist:accompanist-permissions:0.33.2-alpha")

    // Google Maps Compose
    implementation("com.google.maps.android:maps-compose:2.11.5")
    // Google Maps Service
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.1.0")
    // Google Maps Utils
    implementation("com.google.maps.android:android-maps-utils:3.8.2")

    // GLIDE
    implementation("com.github.bumptech.glide:compose:1.0.0-beta01")
    // COIL
    implementation("io.coil-kt:coil-compose:2.5.0")

    // DataStore (Preferences)
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // ROOM
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("com.google.ai.client.generativeai:generativeai:0.2.1")
    implementation("androidx.test:core-ktx:1.5.0")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    // Room: To use Kotlin Symbol Processing (KSP)
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // Reflection API (used for function withoutId() in Photo data class)
    implementation(kotlin("reflect"))


    // Tests
    // JUnit for unit testing
    testImplementation("junit:junit:4.13.2")
    //
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")
    // AndroidX Test - Core & Rules
    // testImplementation ("androidx.test:core:1.5.0")
    // testImplementation ("androidx.test:rules:1.5.0")
    // LiveData and ViewModel testing (InstantTaskExecutorRule)
    testImplementation ("androidx.arch.core:core-testing:2.2.0")
    // Mockito
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")   // ex 5.2.1
//     testImplementation ("org.mockito:mockito-core:5.12.0")
//     testImplementation ("org.mockito:mockito-inline:5.2.0")
    // Robolectric
    testImplementation("org.robolectric:robolectric:4.11.1")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    // Mockito
    androidTestImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
    // For ContentProvider tests
//    androidTestImplementation("androidx.test:core:1.5.0")
//    androidTestImplementation("androidx.test:rules:1.5.0")
//    androidTestImplementation("androidx.test:runner:1.5.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")        // Redundant
//    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
