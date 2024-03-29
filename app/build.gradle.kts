plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "dev.stenglein.connectivitycontroller"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.stenglein.connectivitycontroller"
        minSdk = 24
        targetSdk = 28
        versionCode = 1
        versionName = "1.0.0"

        //testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunner = "dev.stenglein.connectivitycontroller.util.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        base {
            archivesName.set("ConnectivityController-$versionCode-v$versionName")
        }
    }

    lint {
        // For ignoring target sdk error when building
        // Update with gradlew updateLintBaseline
        baseline = file("lint-baseline.xml")
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
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    testOptions {
        managedDevices {
            localDevices {
                create("pixel8proapi34") {
                    device = "Pixel 8 Pro"
                    apiLevel = 34
                    systemImageSource = "google"
                }
                create("pixel6api32") {
                    device = "Pixel 6"
                    apiLevel = 32
                    systemImageSource = "google"
                }
            }

            // Run tests with gradle defaultGroupDebugAndroidTest
            groups {
                // One device after Bluetooth access was restricted and one before
                // Do not test unsupported devices (simple UI test for this)
                create("default") {
                    targetDevices.add(devices["pixel8proapi34"])
                    targetDevices.add(devices["pixel6api32"])
                }
            }
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
    implementation(libs.androidx.lifecycle.runtime.compose)
    androidTestImplementation(libs.androidx.rules)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Hilt
    implementation(libs.hilt.android.core)
    implementation(libs.androidx.hilt.navigation.compose)  // For ViewModel support
    ksp(libs.hilt.compiler)
    kspTest(libs.hilt.compiler)
    kspAndroidTest(libs.hilt.compiler)
    testImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.hilt.android.testing)
}