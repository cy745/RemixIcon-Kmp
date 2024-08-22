pluginManagement {
    repositories {
        maven(url = "https://jitpack.io")
        mavenLocal()
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositories {
        maven(url = "https://jitpack.io")
        mavenLocal()
        google()
        mavenCentral()
    }
}

rootProject.name = "RemixIcon-Kmp"
include(":core")

val isInJitPack = System.getenv()["JITPACK"] == "true"
