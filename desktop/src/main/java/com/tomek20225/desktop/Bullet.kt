package com.tomek20225.desktop

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import kotlin.math.floor

class Bullet(
    private var x: Float,
    private var y: Float,
    private val type: String,
    private val img: Texture,
    private val imgFail: Texture,
    private val imgSuccess: Texture
) {
    private val w: Int = 2
    private val h: Int = 7
    private val playerBulletSpeed: Float = 7f
    private val enemyBulletSpeed: Float = 3.5f
    private var outOfBounds: Boolean = false

    init {
        if (this.type == "PLAYER") {
            this.x += 12
        }
    }

    fun x(): Float = this.x
    fun y(): Float = this.y
    fun width(): Float = this.w.toFloat()
    fun height(): Float = this.h.toFloat()
    fun type(): String = this.type

    fun explode(batch: SpriteBatch) {
        if (this.type == "PLAYER") {
            batch.draw(imgSuccess, this.x - 12, this.y - 12, 26f, 16f)
        } else if (this.type == "ENEMY") {
            batch.draw(imgSuccess, this.x, this.y, 26f, 16f)
        }
    }

    fun explodeFail(batch: SpriteBatch, collided: Boolean = false) {
        if (this.type == "PLAYER") {
            batch.draw(imgFail, this.x, this.y, 30f, 16f)
        } else if (collided || this.type == "ENEMY") {
            batch.draw(imgFail, this.x, this.y - 16, 30f, 16f)
        }
    }

    fun show(batch: SpriteBatch) {
        if (!this.isOutOfBounds()) {
            if (this.type == "PLAYER") {
                batch.draw(img, this.x, this.y, this.w.toFloat(), this.h.toFloat())
            } else if (this.type == "ENEMY") {
                batch.draw(img, this.x, this.y, this.w.toFloat(), this.h.toFloat())
                batch.draw(img, this.x - 2, this.y + floor((this.h / 2).toDouble()).toFloat() - 1, this.w.toFloat() + 4, 2f)
            }
        }
    }

    fun move() {
        if (this.type == "PLAYER") {
            this.y -= this.playerBulletSpeed
            if (this.y < 80) {
                this.outOfBounds = true
            }
        } else if (this.type == "ENEMY") {
            this.y += this.enemyBulletSpeed
            if (this.y > height() - 44) {
                this.outOfBounds = true
            }
        }
    }

    fun collidesWith(bullet: Bullet): Boolean {
        val isBulletHitX = ((bullet.x + bullet.w) >= this.x && bullet.x <= (this.x + this.w))
        val isBulletHitY = (bullet.y <= (this.y + this.h) && (bullet.y + bullet.h) >= this.y)
        return isBulletHitX && isBulletHitY
    }

    fun isOutOfBounds(): Boolean = outOfBounds
}
