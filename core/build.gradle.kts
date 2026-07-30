import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

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
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

// ─── CMP Resources Configuration ────────────────────────────────────────────
compose.resources {
    publicResClass = true
    packageOfResClass = "com.lalilu.remixicon.generated.resources"
    generateResClass = always
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
//    signAllPublications()
}

// Make core compilation depend on SVG resource transformation.
// Ensures composeResources/drawable/*.xml and RemixIcon.kt are generated before compiling.
// Only activate when resources need regeneration:
// tasks.matching { it.name.startsWith("compileKotlin") }.configureEach {
//     dependsOn(rootProject.project(":svg-to-compose").tasks.named("transformSvgIcons"))
// }

// ─── Validation Tasks ───────────────────────────────────────────────────────

val resourcesDir = project.projectDir.resolve("src/commonMain/composeResources/drawable")

/**
 * Verify that the number of generated XML resources matches the number of source SVGs.
 */
tasks.register("validateResourceCount") {
    val svgRootDir = rootProject.projectDir.resolve("RemixIcon/icons")

    doLast {
        val svgCount = svgRootDir.walkTopDown().count { it.extension == "svg" }
        val xmlCount = resourcesDir.walkTopDown().count { it.extension == "xml" }

        check(svgCount == xmlCount) {
            """
            |❌ Resource count mismatch!
            |   SVG files:    $svgCount
            |   XML resources: $xmlCount
            |
            |   Run :svg-to-compose:transformSvgIcons to regenerate resources.
            """.trimMargin()
        }
        println("✅ Resource count OK: $svgCount SVGs → $xmlCount XML resources")
    }
}

/**
 * Verify no duplicate resource file names (flat resource dir must be unique).
 */
tasks.register("validateNoDuplicateResources") {
    doLast {
        if (!resourcesDir.exists()) {
            println("⚠️ Resources directory does not exist: $resourcesDir")
            return@doLast
        }

        val names = resourcesDir.listFiles()
            ?.filter { it.extension == "xml" }
            ?.map { it.nameWithoutExtension }
            ?: emptyList()

        val duplicates = names.groupBy { it }.filter { it.value.size > 1 }
        check(duplicates.isEmpty()) {
            "❌ Duplicate resource names found: ${duplicates.keys.joinToString(", ")}"
        }
        println("✅ No duplicate resource names (${names.size} unique)")
    }
}

/**
 * Verify that the RemixIcon.kt wrapper file exists and looks valid.
 */
tasks.register("validateWrapperExists") {
    doLast {
        val wrapperFile = project.projectDir.resolve("src/commonMain/kotlin/com/lalilu/RemixIcon.kt")
        check(wrapperFile.exists()) {
            "❌ RemixIcon.kt wrapper not found at: $wrapperFile"
        }

        val content = wrapperFile.readText()
        check(content.contains("object RemixIcon")) {
            "❌ RemixIcon.kt does not contain 'object RemixIcon'"
        }
        check(content.contains("DrawableResource")) {
            "❌ RemixIcon.kt does not reference DrawableResource"
        }

        println("✅ RemixIcon.kt wrapper exists and looks valid (${content.lines().size} lines)")
    }
}

/**
 * Run all validations.
 */
tasks.register("validateAll") {
    dependsOn("validateResourceCount", "validateNoDuplicateResources", "validateWrapperExists")
    doLast {
        println()
        println("✅ All validations passed!")
    }
}

// Make transformSvgIcons run validateAll afterwards
rootProject.tasks.matching { it.name == "transformSvgIcons" }.configureEach {
    finalizedBy(":core:validateAll")
}
