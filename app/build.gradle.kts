plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.catalogofthings"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.catalogofthings"
        minSdk = 26
        targetSdk = 36
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("dev.androidbroadcast.vbpd:vbpd-reflection:2.0.4")

    val navigation = "2.9.5"
    implementation("androidx.navigation:navigation-fragment-ktx:${navigation}")
    implementation("androidx.navigation:navigation-ui-ktx:${navigation}")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:${navigation}")

    implementation("com.google.dagger:dagger:2.56.2")
    kapt("com.google.dagger:dagger-compiler:2.56.2")

    implementation("androidx.room:room-runtime:2.8.4")
    implementation("androidx.room:room-ktx:2.8.4")
    ksp("androidx.room:room-compiler:2.8.4")
}