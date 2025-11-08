plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dragger.hilt.android)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.gms.google.services)
}

android {
    namespace = "com.kevinduran.myshop"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.kevinduran.myshop"
        minSdk = 29
        targetSdk = 35
        versionCode = 2
        versionName = "1.2.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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
        compose = true
    }
}

dependencies {

    //Icons
    implementation(libs.icons.lucide)
    //Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    //Gson
    implementation(libs.gson)
    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    //hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    //Data store
    implementation(libs.androidx.datastore.preferences)
    //Material
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.material.icons.extended.android)
    //Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.compose.network)
    //Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.database.ktx)
    implementation(libs.firebase.storage.ktx)
    //Play services auth
    implementation(libs.play.services.auth)
    //Lottie
    implementation(libs.lottie.compose)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.runtime)
    implementation(libs.core)
    implementation(libs.compose.material.dialogs.datetime)
    implementation(libs.androidx.animation)
    //Others
    implementation(libs.kotlinx.coroutines.play.services)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
