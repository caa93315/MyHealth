plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.health"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.health"
        minSdk = 26 // Health Connect 建議最低 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // 為了避免你之前的 C++ 設定報錯，先保留這塊
        externalNativeBuild {
            cmake {
                cppFlags += ""
            }
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    // C++ 設定保留
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
}

dependencies {
    // ============================================================
    // ★ 關鍵修正區：手動指定版本，解決 "requires compileSdk 36" 錯誤
    // ============================================================

    // 將 core-ktx 從 1.17.0 (不穩定) 降級到 1.15.0 (穩定)
    implementation("androidx.core:core-ktx:1.15.0")

    // 將 activity-compose 從 1.11.0 (不穩定) 降級到 1.9.3 (穩定)
    implementation("androidx.activity:activity-compose:1.9.3")

    // 其他基礎組件
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.viewpager2:viewpager2:1.1.0")

    // ============================================================
    // ★ Compose 設定：使用穩定的 BOM 版本
    // ============================================================
    implementation(platform("androidx.compose:compose-bom:2024.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // ============================================================
    // ★ Health Connect 設定
    // ============================================================
    // 使用目前可用的 Alpha 版本 (因為你之前回報 1.0.0 下載失敗)
    implementation("androidx.health.connect:connect-client:1.1.0-alpha07")

    // ============================================================
    // ★ 其他與測試
    // ============================================================
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.10.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    // ★ MPAndroidChart 圖表庫
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

}