package com.tomek20225.desktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle

class Player(val texture: Texture) {
    var x: Float
    val y: Float
    private val w: Int
    private val h: Int
    private val speed: Float
    private val img: Texture
    private val bounds: Rectangle

    init {
        this.w = 26
        this.h = 16
        this.x = (Gdx.graphics.width / 2f) - (this.w / 2f)
        this.y = (Gdx.graphics.height - 88).toFloat()
        this.speed = 2.5f
        this.img = Texture(Gdx.files.internal("player.png"))
        this.bounds = Rectangle(x, y, w.toFloat(), h.toFloat())
    }

    fun show(batch: Batch) {
        batch.draw(this.img, this.x, this.y, this.w.toFloat(), this.h.toFloat())
    }

    fun move(dir: String) {
        val currentSpeed = if (dir == "LEFT") -this.speed else this.speed
        val newX = this.x + currentSpeed
        if (newX >= 0 && newX + this.w <= Gdx.graphics.width) {
            bounds.x = newX
        }
    }

    fun isHit(bullet: Bullet): Boolean {
        if (bounds.overlaps(bullet.bounds)) {
            Gdx.app.log("Game", "Player has been destroyed!")
            return true
        }
        return false
    }

    fun draw(batch: SpriteBatch) {
        batch.draw(texture, x, y)
    }

    fun update(delta: Float) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= speed * delta
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += speed * delta
        }

        // Ensure the player stays within the screen bounds
        if (x < 0) x = 0f
        if (x > Gdx.graphics.width - texture.width) x = (Gdx.graphics.width - texture.width).toFloat()

        // Update bounds
        bounds.setPosition(x, y)
    }

    fun dispose() {
        img.dispose()
    }
}
