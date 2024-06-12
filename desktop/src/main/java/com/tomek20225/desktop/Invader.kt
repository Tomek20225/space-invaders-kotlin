package com.tomek20225.desktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import kotlin.math.abs
import kotlin.math.floor

open class Invader(
    private var realX: Float,
    private var y: Float,
    private val w: Int,
    private val h: Int,
    private val img0: Texture,
    private val img1: Texture,
    private val points: Int
) {
    private var x: Int = floor(realX).toInt()
    private var state: Int = 0
    private val pxDiff: Int = 2

    fun x(): Float = x.toFloat()

    fun y(): Float = y

    fun width(): Float = w.toFloat()

    fun height(): Float = h.toFloat()

    fun getPoints(): Int = points

    private fun switchState() {
        state = if (state == 0) 1 else 0
    }

    fun show(batch: SpriteBatch) {
        batch.begin()
        val currentImage = if (state == 0) img0 else img1
        batch.draw(currentImage, x.toFloat(), y, w.toFloat(), h.toFloat())
        batch.end()
    }

    fun move(dir: String, speed: Float) {
        val actualSpeed = if (dir == "LEFT") -speed else speed
        realX += actualSpeed

        val prevX = x
        val newX = floor(realX).toInt()

        if (abs(newX - prevX) >= pxDiff) {
            x = newX
            switchState()
        }
    }

    fun moveDown(dist: Int) {
        y -= dist
    }
}
