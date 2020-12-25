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
version = "0.1.0"

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

    implementation(project(":rvbaseadapter"))

    //kotlin
    implementation(deps(Kotlin.stdlib_jdk8))

    //paging
    implementation(deps(AndroidX.paging_runtime_ktx))
    implementation(deps(AndroidX.paging_rxjava2_ktx))

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
