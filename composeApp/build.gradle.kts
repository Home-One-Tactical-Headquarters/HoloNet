import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

val pluginsDir: File by rootProject.extra

buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
        classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.17.0")
    }
}

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    id("com.codingfeline.buildkonfig") version "+"
    kotlin("plugin.serialization") version "2.1.0"
}

kotlin {
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting
        
        commonMain.dependencies {
            // Compose
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            // Lifecycle and ViewModel
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            // PF4J
            implementation(libs.pf4j)

            // Serialization
            implementation(libs.kotlinx.serialization.json)

            // Koin
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // Ktor
            implementation(project.dependencies.platform(libs.ktor.bom))
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.server.core)
            implementation(libs.ktor.server.netty)
            implementation(libs.ktor.server.content.negotiation)
            implementation(libs.ktor.server.cors)
            implementation(libs.ktor.serialization.kotlinx.json)

            // Coil
            implementation(libs.coil.compose)
            implementation(libs.coil.okhttp)

            // Holonet Core
            implementation(libs.holonet.core)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}


compose.desktop {
    application {
        mainClass = "dk.holonet.MainKt"

        // If project.version contains beta or rc it will be treated as a pre-release version
        // Strip out any -beta or -rc suffixes for the version used in the installer and appended it to packageName
        val versionParts = project.version.toString().split("-")
        val isPreRelease = versionParts.size > 1 && (versionParts[1].startsWith("beta") || versionParts[1].startsWith("rc"))
        val versionName = versionParts[0]
        val preReleaseSuffix = if (isPreRelease) {
            when {
                versionParts[1].startsWith("beta") -> "-beta"
                versionParts[1].startsWith("rc") -> "-rc"
                else -> ""
            }
        } else ""

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "HoloNet${preReleaseSuffix}"
            packageVersion = versionName
        }
    }
}

buildkonfig {
    packageName = "dk.holonet.config"
    // objectName = "YourAwesomeConfig"
    // exposeObjectWithName = "YourAwesomePublicConfig"

    defaultConfigs {
        buildConfigField(STRING, "pluginsDir", pluginsDir.path)
    }
}

tasks.register("printVersion") {
    doLast {
        println("${project.version}")
    }
}



