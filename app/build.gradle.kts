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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true  // Added to allow BuildConfig import in Compose
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildToolsVersion = "34.0.0"
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    //For PickVisualMedia contract
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.10.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    // Using icons extension, make sure to use R8 / ProGuard to remove unused icons from this application
    implementation("androidx.compose.material:material-icons-extended:1.5.4")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class:1.1.2")
    // DatePickerColors with complete customized colors
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.compose.material3:material3-adaptive:1.0.0-alpha04")
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.0.0-alpha02")
    //
    implementation("androidx.navigation:navigation-compose:2.7.6")
    // Accompanist-Permissions
    implementation("com.google.accompanist:accompanist-permissions:0.33.2-alpha")

    // Google Maps Compose
    implementation("com.google.maps.android:maps-compose:2.11.5")
    // Google Maps Service
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    // Google Maps Utils
    implementation("com.google.maps.android:android-maps-utils:3.8.0")

    // GLIDE
    implementation("com.github.bumptech.glide:compose:1.0.0-beta01")
    // COIL
    implementation("io.coil-kt:coil-compose:2.5.0")

    // DataStore (Preferences)
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // ROOM
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    // Room: To use Kotlin Symbol Processing (KSP)
    ksp("androidx.room:room-compiler:2.6.1")
    // implementation("androidx.room:room-ktx:2.6.1")


    // Tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
