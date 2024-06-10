package com.tomek20225.desktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import kotlin.random.Random

open class PieceCollection(
    var x: Float,
    var y: Float,
    var w: Int,
    var h: Int
) {
    val pxScale: Int = 2
    val pieces: Array<BooleanArray> = Array(h) { BooleanArray(w) }
    private val fill = com.badlogic.gdx.graphics.Color(51 / 255f, 183 / 255f, 60 / 255f, 1f)

    init {
        this.y = Gdx.graphics.height - y - h * pxScale
    }
    fun show(shapeRenderer: ShapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = fill
        for (y in 0 until this.h * this.pxScale step this.pxScale) {
            for (x in 0 until this.w * this.pxScale step this.pxScale) {
                if (this.pieces[y / this.pxScale][x / this.pxScale]) {
                    val pieceX = this.x + x
                    val pieceY = this.y + y
                    shapeRenderer.rect(pieceX, pieceY, this.pxScale.toFloat(), this.pxScale.toFloat())
                }
            }
        }
        shapeRenderer.end()
    }

    fun isDestroyed(): Boolean {
        for (y in 0 until this.h) {
            for (x in 0 until this.w) {
                if (this.pieces[y][x]) {
                    return false
                }
            }
        }
        return true
    }

    private fun isPieceHit(x: Int, y: Int, bullet: Bullet): Boolean {
        if (this.pieces[y][x]) {
            val pieceX = this.x + (x * this.pxScale)
            val pieceY = this.y + (y * this.pxScale)
            val isPieceHitX = ((bullet.x() + bullet.width()) >= pieceX && bullet.x() <= (pieceX + this.pxScale))
            val isPieceHitY = (bullet.y() <= (pieceY + this.pxScale) && (bullet.y() + bullet.height()) >= pieceY)
            if (isPieceHitX && isPieceHitY) {
                for (yp in y - 2..y + 2) {
                    for (xp in x - 2..x + 2) {
                        if (yp in 0 until this.h && xp in 0 until this.w) {
                            if (xp == x - 2 || xp == x + 2 || yp == y - 2 || yp == y + 2) {
                                if (Random.nextFloat() >= 0.6) {
                                    this.pieces[yp][xp] = false
                                }
                            } else {
                                this.pieces[yp][xp] = false
                            }
                        }
                    }
                }
                return true
            }
        }
        return false
    }

    fun isHit(bullet: Bullet): Boolean {
        if (this.isDestroyed()) {
            return false
        }

        if (bullet.type() == "ENEMY") {
            for (y in 0 until this.h) {
                for (x in 0 until this.w) {
                    if (this.isPieceHit(x, y, bullet)) {
                        println("[Barriers & Floors] Piece hit by the enemy!")
                        return true
                    }
                }
            }
        } else if (bullet.type() == "PLAYER") {
            for (y in this.h - 1 downTo 0) {
                for (x in 0 until this.w) {
                    if (this.isPieceHit(x, y, bullet)) {
                        println("[Barriers & Floors] Piece hit by the player!")
                        return true
                    }
                }
            }
        }
        return false
    }
}
