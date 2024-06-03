plugins {
    id("java-library")
    alias(libs.plugins.jetbrainsKotlinJvm)
    application
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.badlogicgames.gdx:gdx:1.10.0")
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:1.10.0")
    implementation("com.badlogicgames.gdx:gdx-platform:1.10.0:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-freetype:3.3.0")

    // LWJGL dependencies
    implementation("org.lwjgl:lwjgl:3.3.0")
    implementation("org.lwjgl:lwjgl-glfw:3.3.0")
    implementation("org.lwjgl:lwjgl-opengl:3.3.0")
    implementation("org.lwjgl:lwjgl-stb:3.3.0")

    // LWJGL native dependencies for MacOS
    runtimeOnly("org.lwjgl:lwjgl:3.3.0:natives-macos")
    runtimeOnly("org.lwjgl:lwjgl-glfw:3.3.0:natives-macos")
    runtimeOnly("org.lwjgl:lwjgl-opengl:3.3.0:natives-macos")
    runtimeOnly("org.lwjgl:lwjgl-stb:3.3.0:natives-macos")
    runtimeOnly("org.lwjgl:lwjgl-openal:3.3.1:natives-macos")

    // LWJGL native dependencies for MacOS aarch64
    runtimeOnly("org.lwjgl:lwjgl:3.3.1:natives-macos-arm64")
    runtimeOnly("org.lwjgl:lwjgl-glfw:3.3.1:natives-macos-arm64")
    runtimeOnly("org.lwjgl:lwjgl-opengl:3.3.1:natives-macos-arm64")
    runtimeOnly("org.lwjgl:lwjgl-stb:3.3.1:natives-macos-arm64")
    runtimeOnly("org.lwjgl:lwjgl-openal:3.3.1:natives-macos-arm64")

    // LWJGL native dependencies for Windows x64
    runtimeOnly("org.lwjgl:lwjgl:3.3.1:natives-windows")
    runtimeOnly("org.lwjgl:lwjgl-glfw:3.3.1:natives-windows")
    runtimeOnly("org.lwjgl:lwjgl-opengl:3.3.1:natives-windows")
    runtimeOnly("org.lwjgl:lwjgl-stb:3.3.1:natives-windows")
    runtimeOnly("org.lwjgl:lwjgl-openal:3.3.1:natives-windows")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
}
