pluginManagement {
    repositories {
        maven(url = "https://jitpack.io")
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "RemixIcon-Kmp"
include(":core")
include(":svg-to-compose")

val isInJitPack = System.getenv()["JITPACK"] == "true"
