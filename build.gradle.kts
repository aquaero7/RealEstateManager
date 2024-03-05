// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.3.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    // Room: To use Kotlin Symbol Processing (KSP)
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false
}

// secrets-gradle-plugin
buildscript {
    dependencies {
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
        // Room: To use Kotlin Symbol Processing (KSP)
        classpath(kotlin("gradle-plugin", version = "1.9.22"))
    }
}