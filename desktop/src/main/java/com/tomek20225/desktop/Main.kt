package com.tomek20225.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

fun main() {
    val config = Lwjgl3ApplicationConfiguration().apply {
        setTitle("Space Invaders")
        setWindowedMode(800, 600)
        setForegroundFPS(60)
    }
    Lwjgl3Application(Game(), config)
}
