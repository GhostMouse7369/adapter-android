import top.laoshuzi.dependencies.AndroidBuildConfig
import top.laoshuzi.dependencies.deps.*
import top.laoshuzi.dependencies.publish.MavenPublish.mavenAndroidPublication
import top.laoshuzi.dependencies.publish.MavenPublish.mavenRepository

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    `maven-publish`
}

group = "top.laoshuzi.android"
version = AndroidBuildConfig.version_name

val ziweiRepositoryDomainName: String by project
val ziweiRepositoryUsername: String by project
val ziweiRepositoryPassword: String by project

android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    compileSdkVersion(AndroidBuildConfig.target_sdk)
    defaultConfig {
        minSdkVersion(AndroidBuildConfig.min_sdk)
        targetSdkVersion(AndroidBuildConfig.target_sdk)
        versionCode = AndroidBuildConfig.version_code
        versionName = AndroidBuildConfig.version_name
        testInstrumentationRunner = AndroidBuildConfig.test_instrumentation_runner
        consumerProguardFiles(AndroidBuildConfig.consumer_file)
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                AndroidBuildConfig.proguard_file
            )
        }
    }
}

dependencies {
    api(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    //kotlin
    implementation(deps(Kotlin.stdlib_jdk8))

    //component
    implementation(deps(AndroidX.recyclerview))

}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("mavenAndroid") {
                mavenAndroidPublication(project)
            }
        }
        repositories {
            maven {
                mavenRepository(
                    project,
                    ziweiRepositoryDomainName,
                    ziweiRepositoryUsername,
                    ziweiRepositoryPassword
                )
            }
        }
    }
}
