package com.tomek20225.desktop
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

class Main : ApplicationAdapter() {
    override fun create() {
        // Initialize game resources here
    }

    override fun render() {
        // Game loop logic goes here
    }

    override fun dispose() {
        // Dispose game resources here
    }
}

fun main() {
    val config = Lwjgl3ApplicationConfiguration().apply {
        setTitle("Space Invaders")
        setWindowedMode(800, 600)
        setForegroundFPS(60)
    }
    Lwjgl3Application(Main(), config)
}
