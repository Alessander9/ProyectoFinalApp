plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.proyecto_app_cbt"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.proyecto_app_cbt"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Dependencias actualizadas para compatibilidad y últimas versiones estables:
    implementation("androidx.core:core-ktx:1.13.1") // Actualizado a la versión 1.13.1
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0") // Actualizado a la versión 1.12.0
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Media3 (ExoPlayer moderno) - Mantenidas como estaban
    implementation("androidx.media3:media3-exoplayer:1.3.0")
    implementation("androidx.media3:media3-ui:1.3.0")
    implementation(libs.androidx.activity) // Mantenida como estaba, asumiendo que está definida en libs.versions.toml

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}