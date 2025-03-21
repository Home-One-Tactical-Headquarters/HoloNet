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
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.pf4j)
            implementation(libs.holonet.core)
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
            implementation("io.insert-koin:koin-core:4.0.2")
            implementation("io.insert-koin:koin-compose:4.0.2")
            implementation("io.ktor:ktor-client-core:3.1.0")
            implementation("io.ktor:ktor-client-cio:3.1.0")
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

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "dk.holonet"
            packageVersion = "1.0.0"
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


