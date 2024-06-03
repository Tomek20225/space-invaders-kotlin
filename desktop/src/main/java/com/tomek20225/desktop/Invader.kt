package com.tomek20225.desktop

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

open class Invader(protected val texture: Texture, position: Vector2, protected val speed: Float) {
    var position: Vector2 = position
        private set
    val bounds: Rectangle = Rectangle(position.x, position.y, texture.width.toFloat(), texture.height.toFloat())

    open fun update(delta: Float) {
        position.x += speed * delta
        bounds.setPosition(position.x, position.y)
    }

    open fun draw(batch: SpriteBatch) {
        batch.draw(texture, position.x, position.y)
    }
}
