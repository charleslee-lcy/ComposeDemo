plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.baidu.aip.asrwakeup3.core"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        targetSdk = 34
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

    kotlinOptions {
        jvmTarget = "1.8"
    }

    packagingOptions {
        doNotStrip("*/*/libvad.dnn.so")
        doNotStrip("*/*/libbd_easr_s1_merge_normal_20151216.dat.so")
    }
}



dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    api(files("libs/bdasr_V3_20210628_cfe8c44.jar"))
    api(files("libs/com.baidu.tts_2.6.2.2.20200629_44818d4.jar"))
}
