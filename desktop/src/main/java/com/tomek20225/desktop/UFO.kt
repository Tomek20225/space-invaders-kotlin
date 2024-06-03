package com.tomek20225.desktop

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2

class UFO(private val texture: Texture, position: Vector2, private val speed: Float) {
    var position: Vector2 = position
        private set

    fun update(delta: Float) {
        position.x += speed * delta
    }

    fun draw(batch: SpriteBatch) {
        batch.draw(texture, position.x, position.y)
    }
}
