import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}
var properties = Properties()
properties.load(FileInputStream("gradle.properties"))

android {
    namespace = "com.example.myroadmap"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myroadmap"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        ndk {
            abiFilters.add("arm64-v8a")
            abiFilters.add("armeabi-v7a")
            abiFilters.add("x86")
            abiFilters.add("x86_64")
        }
        buildConfigField("String", "AUTHORIZATION_KEY", properties.getProperty("AUTHORIZATION_KEY"))
        buildConfigField("String", "NATIVE_APP_KEY", properties.getProperty("NATIVE_APP_KEY"))
        manifestPlaceholders["NATIVE_APP_KEY"] = properties["NATIVE_APP_KEY"] as String
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
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.play.services.maps)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.awareness)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.ui.test.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.compose.ui:ui:1.7.3")
    implementation("androidx.compose.material:material:1.7.3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.3")
//    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0")
    implementation("androidx.activity:activity-compose:1.6.0")
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation("androidx.compose.ui:ui:1.3.0")
    implementation("androidx.compose.material:material:1.3.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.3.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0") // 추가된 의존성
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0") // 코루틴 의존성
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")

    implementation(libs.androidx.lifecycle.runtime.ktx.v260)
    // 스플래시 화면
    implementation(libs.androidx.core.splashscreen)

    // 카카오맵
    implementation ("com.kakao.maps.open:android:2.11.9")

    implementation (libs.retrofit)
    implementation (libs.converter.gson)

}