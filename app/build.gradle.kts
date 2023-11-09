plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.heissen.cragexplorer"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.heissen.cragexplorer"
        minSdk = 33
        targetSdk = 33
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
        debug {

            isDebuggable=true;
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment:2.5.3")
    implementation("androidx.navigation:navigation-ui:2.5.3")
    implementation ("com.google.android.gms:play-services-location:20.0.0")
    implementation ("com.google.android.gms:play-services-maps:18.1.0")
    
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")



    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    implementation ("com.squareup.retrofit2:retrofit:2.6.2")
    implementation ("com.squareup.retrofit2:converter-gson:2.6.2")
    implementation ("me.zhanghai.android.materialratingbar:library:1.4.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.github.f0ris.sweetalert:library:1.5.6")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.0")
    implementation("com.google.firebase:firebase-auth")
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
}