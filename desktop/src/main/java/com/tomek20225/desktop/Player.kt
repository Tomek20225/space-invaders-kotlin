package com.tomek20225.desktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Player(private val img: Texture) {
    private var x: Float = (Gdx.graphics.width / 2 - 13).toFloat()
    private val y: Float = 70f
    private val w: Int = 26
    private val h: Int = 16
    private val speed: Float = 2.5f

    fun x(): Float = this.x
    fun y(): Float = this.y
    fun show(batch: SpriteBatch) {
        batch.begin()
        batch.draw(this.img, this.x, this.y, this.w.toFloat(), this.h.toFloat())
        batch.end()
    }

    fun move(dir: String) {
        val currentSpeed = if (dir == "LEFT") -this.speed else this.speed
        if (this.x + currentSpeed >= 0 && this.x + currentSpeed + this.w <= Gdx.graphics.width) {
            this.x += currentSpeed
        }
    }

    fun isHit(bullet: Bullet): Boolean {
        val isPlayerHitX = ((bullet.x() + bullet.width()) >= this.x && bullet.x() <= (this.x + this.w))
        val isPlayerHitY = (bullet.y() <= (this.y + this.h) && (bullet.y() + bullet.height()) >= this.y)

        if (isPlayerHitX && isPlayerHitY) {
            println("[Game] Player has been destroyed!")
            return true
        }
        return false
    }
}
