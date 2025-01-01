import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.vanniktech.publish)
    alias(libs.plugins.dokka)
}

kotlin {
    jvm()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    androidTarget {
        publishLibraryVariants("release")
    }

    js {
        browser()
        nodejs()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        nodejs()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
        }
    }
}

group = libs.versions.lib.group.get()
version = libs.versions.lib.version.get()

android {
    namespace = "$group".replace('-', '_').lowercase()
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

mavenPublishing {
    coordinates(
        groupId = group.toString(),
        artifactId = "remixicon-kmp",
        version = version.toString()
    )

    configure(
        KotlinMultiplatform(
            javadocJar = JavadocJar.Dokka("dokkaHtml"),
            sourcesJar = true,
            androidVariantsToPublish = listOf("release")
        )
    )

    pom {
        name = "RemixIcon Kmp"
        description = "Kotlin Multiplatform library for the icon set of RemixIcon"
        inceptionYear = "2024"
        url = "https://github.com/cy745/RemixIcon-Kmp/"

        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "http://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }

        developers {
            developer {
                id = "cy745"
                name = "cy745"
                url = "https://github.com/cy745/"
            }
        }

        scm {
            url = "https://github.com/cy745/RemixIcon-Kmp/"
            connection = "scm:git:git://github.com/cy745/RemixIcon-Kmp.git"
            developerConnection = "scm:git:ssh://git@github.com/cy745/RemixIcon-Kmp.git"
        }
    }

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
}
