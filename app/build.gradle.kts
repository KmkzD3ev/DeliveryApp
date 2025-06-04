plugins {
    id("org.jetbrains.kotlin.android")
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.deliveryapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.deliveryapp"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    viewBinding{
        enable = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:2.9.9")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.google.firebase:firebase-storage-ktx")
    // Você tinha firebase-analytics-ktx e firebase-auth-ktx duas vezes, removi a duplicação para clareza
    // implementation("com.google.firebase:firebase-analytics-ktx")
    // implementation("com.google.firebase:firebase-auth-ktx")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation ("com.google.android.gms:play-services-auth:20.4.0")
    implementation ("com.google.firebase:firebase-firestore-ktx")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("com.google.code.gson:gson:2.9.0")
    // Você tinha androidx.browser:browser duas vezes com versões diferentes, mantive a mais nova
    // implementation ("androidx.browser:browser:1.4.0")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.retrofit2:converter-scalars:2.4.0")

    implementation("com.mercadopago.android.px:checkout:4.53.2")
    implementation("androidx.browser:browser:1.8.0")


    implementation ("com.google.firebase:firebase-firestore-ktx")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")


    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    configurations.all {
        resolutionStrategy {
            // Força todas as dependências da família androidx.lifecycle a usarem a versão 2.7.0
            force("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
            force("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
            force("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
            force("androidx.lifecycle:lifecycle-common-java8:2.7.0")
            force("androidx.lifecycle:lifecycle-process:2.7.0")
            force("androidx.lifecycle:lifecycle-service:2.7.0")
            force("androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0")
            force("androidx.lifecycle:lifecycle-reactivestreams-ktx:2.7.0")
            // Se o erro persistir e mencionar outras do lifecycle, adicione-as aqui também.
        }
    }
    // =========================================================================

}