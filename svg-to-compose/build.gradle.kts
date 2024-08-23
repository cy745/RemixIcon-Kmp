plugins {
    id("kotlin")
    alias(libs.plugins.build.config)
    id("application")
}

buildConfig {
    // 传递根项目路径
    buildConfigField(String::class.java, "rootPath", rootProject.projectDir.absolutePath)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
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

tasks.register<JavaExec>("transformSvgIcons") {
    dependsOn("build")

    group = "Execution"
    description = "Transforms the SVG icons to Compose"

    classpath = sourceSets.getByName("test").runtimeClasspath

    mainClass = "br.com.devsrsouza.svg2compose.MainKt"
}
