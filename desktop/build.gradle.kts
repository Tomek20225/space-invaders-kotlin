plugins {
    id("java-library")
    alias(libs.plugins.jetbrainsKotlinJvm)
    application
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.badlogicgames.gdx:gdx:1.10.0")
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:1.10.0")
    implementation("com.badlogicgames.gdx:gdx-platform:1.10.0:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-freetype:1.10.0")
    implementation("com.badlogicgames.gdx:gdx-freetype-platform:1.10.0:natives-desktop")

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

sourceSets {
    main {
        resources.srcDirs("src/main/resources", "../core/assets")
    }
}

application {
    mainClass.set("com.tomek20225.desktop.MainKt")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "com.tomek20225.desktop.MainKt"
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}

tasks.register<Copy>("copyNatives") {
    from(configurations.runtimeClasspath.get().filter { it.name.contains("natives") })
    into("$buildDir/libs/natives")
}
