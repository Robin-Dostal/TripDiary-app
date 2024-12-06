plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.traveldiary"
    compileSdk = 34

    packaging{
        resources {
            excludes += "META-INF/native-image/**"
        }
    }

    buildFeatures {
        viewBinding = true
    }

    viewBinding {
        enable = true
    }

    defaultConfig {
        applicationId = "com.example.traveldiary"
        minSdk = 28
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    //implementation("androidx.navigation:navigation-fragment-ktx:2.8.4")
    //implementation("androidx.navigation:navigation-ui-ktx:2.8.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

    //mongodb
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:5.2.0")
    implementation("org.mongodb:bson-kotlin:5.2.0")
    implementation ("org.litote.kmongo:kmongo:4.9.0")


    implementation ("org.slf4j:slf4j-simple:2.0.9")
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.7.3")
    implementation ("io.projectreactor:reactor-core:3.5.11")
    implementation ("org.jetbrains.kotlin:kotlin-reflect:1.9.10")

    implementation("io.realm.kotlin:library-sync:1.6.0")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    //WorkManager
    implementation ("androidx.work:work-runtime-ktx:2.8.1")
    implementation ("com.google.guava:guava:31.1-android")
}
