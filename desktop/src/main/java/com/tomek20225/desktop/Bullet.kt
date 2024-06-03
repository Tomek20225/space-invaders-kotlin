package com.tomek20225.desktop

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class Bullet(private val texture: Texture, position: Vector2, private val speed: Float, private val bulletType: String) {
    var position: Vector2 = position
        private set
    val bounds: Rectangle = Rectangle(position.x, position.y, texture.width.toFloat(), texture.height.toFloat())

    fun update(delta: Float) {
        position.y += speed * delta
        bounds.setPosition(position.x, position.y)
    }

    fun draw(batch: SpriteBatch) {
        batch.draw(texture, position.x, position.y)
    }

    fun x(): Float = position.x
    fun y(): Float = position.y
    fun width(): Float = texture.width.toFloat()
    fun height(): Float = texture.height.toFloat()
    fun type(): String = bulletType
}
