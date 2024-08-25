// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:8.0.2")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20")
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.1")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}


