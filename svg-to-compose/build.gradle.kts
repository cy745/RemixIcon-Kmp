plugins {
    id("kotlin")
}

group = "br.com.devsrsouza"
version = "0.7.0"

dependencies {
    implementation("com.google.guava:guava:23.0")
    implementation("com.android.tools:sdk-common:27.2.0-alpha16")
    implementation("com.android.tools:common:27.2.0-alpha16")
    implementation("com.squareup:kotlinpoet:1.9.0")
    implementation("org.ogce:xpp3:1.1.6")

    testImplementation(libs.junit)
}

