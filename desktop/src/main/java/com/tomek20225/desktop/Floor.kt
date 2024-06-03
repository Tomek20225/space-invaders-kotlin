package com.tomek20225.desktop

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class Floor(private val texture: Texture, position: Vector2) {
    val bounds: Rectangle = Rectangle(position.x, position.y, texture.width.toFloat(), texture.height.toFloat())

    fun draw(batch: SpriteBatch) {
        batch.draw(texture, bounds.x, bounds.y)
    }
}
