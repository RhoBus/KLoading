import com.android.build.api.dsl.androidLibrary
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "io.github.rhobus"
version = "1.0.0"

kotlin {
    jvm("desktop")
    androidLibrary {
        namespace = "io.github.rhobus.kloading"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        withJava() // enable java compilation support
        withHostTestBuilder {}.configure {}
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }

        compilations.configureEach {
            compilerOptions.configure {
                jvmTarget.set(
                    JvmTarget.JVM_11
                )
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)        // optional
            implementation(compose.animation)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(group.toString(), "KLoading", version.toString())

    pom {
        name = "KLoading"
        description = "KLoading is a Compose Multiplatform library offering a collection of beautiful and customizable loading animations. Easily implement loading states across Android, iOS, Desktop, and Web."
        inceptionYear = "2025"
        url = "https://github.com/RhoBus/KLoading/"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "repo"
            }
        }
        developers {
            developer {
                id = "rhobus"
                name = "Rodrigo Sander Bustillos Vargas"
                url = "https://github.com/RhoBus/"
            }
        }
        scm {
            url = "https://github.com/RhoBus/KLoading/"
            connection = "scm:git:https://github.com/RhoBus/KLoading.git"
            developerConnection = "scm:git:ssh://git@github.com/RhoBus/KLoading.git"
        }
    }
}
