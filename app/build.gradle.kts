import java.util.Properties

plugins {
    id("com.android.application") version "9.3.0"
}

android {
    namespace = "org.animalwellnessaction.app"
    compileSdk = 37

    defaultConfig {
        applicationId = "org.animalwellnessaction.app"
        minSdk = 24
        targetSdk = 36
        versionCode = 4
        versionName = "1.0.2"
    }

    signingConfigs {
        create("release") {
            val props = Properties()
            val localPropertiesFile = rootProject.file("local.properties")
            if (localPropertiesFile.exists()) {
                localPropertiesFile.inputStream().use { stream ->
                    props.load(stream)
                }
            }

            val path = props.getProperty("signing.storeFile")
            if (!path.isNullOrBlank()) {
                storeFile = file(path)
            }
            storePassword = props.getProperty("signing.storePassword")
            keyAlias = props.getProperty("signing.keyAlias")
            keyPassword = props.getProperty("signing.keyPassword")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.19.0")
    implementation("androidx.core:core-splashscreen:1.2.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.webkit:webkit:1.16.0")
    implementation("androidx.work:work-runtime-ktx:2.11.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
}
