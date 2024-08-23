import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.vanniktech.publish) apply false
}

allprojects {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_1_8
        }
    }
    tasks.withType(JavaCompile::class).configureEach {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }
}

if (System.getenv("JITPACK") == "true") {
    project("core") {
        afterEvaluate {
            tasks.getByName("clean") {
                dependsOn(":svg-to-compose:transformSvgIcons")
            }
        }
    }
}