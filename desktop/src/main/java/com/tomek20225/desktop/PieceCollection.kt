package com.tomek20225.desktop

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

class PieceCollection(
    private var x: Float,
    private var y: Float,
    private val w: Int,
    private val h: Int
) {
    private val pxScale: Int = 2
    private val fill: Color = Color(51 / 255f, 183 / 255f, 60 / 255f, 1f)
    private val pieces: Array<BooleanArray> = Array(h) { BooleanArray(w) }

    fun show(batch: SpriteBatch, shapeRenderer: ShapeRenderer) {
        shapeRenderer.color = fill
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        for (y in 0 until h * pxScale step pxScale) {
            for (x in 0 until w * pxScale step pxScale) {
                if (pieces[y / pxScale][x / pxScale]) {
                    val pieceX = this.x + x
                    val pieceY = this.y + y
                    shapeRenderer.rect(pieceX, pieceY, pxScale.toFloat(), pxScale.toFloat())
                }
            }
        }
        shapeRenderer.end()
    }

    fun isDestroyed(): Boolean {
        for (y in 0 until h) {
            for (x in 0 until w) {
                if (pieces[y][x]) {
                    return false
                }
            }
        }
        return true
    }

    private fun isPieceHit(x: Int, y: Int, bullet: Bullet): Boolean {
        if (pieces[y][x]) {
            val pieceX = this.x + (x * pxScale)
            val pieceY = this.y + (y * pxScale)
            val isPieceHitX = (bullet.x() + bullet.width() >= pieceX && bullet.x() <= pieceX + pxScale)
            val isPieceHitY = (bullet.y() <= pieceY + pxScale && bullet.y() + bullet.height() >= pieceY)

            if (isPieceHitX && isPieceHitY) {
                for (yp in y - 2..y + 2) {
                    for (xp in x - 2..x + 2) {
                        if (yp in 0 until h && xp in 0 until w) {
                            if (xp == x - 2 || xp == x + 2 || yp == y - 2 || yp == y + 2) {
                                if (Math.random() >= 0.6) {
                                    pieces[yp][xp] = false
                                }
                            } else {
                                pieces[yp][xp] = false
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
        if (isDestroyed()) {
            return false
        }

        if (bullet.type() == "ENEMY") {
            for (y in 0 until h) {
                for (x in 0 until w) {
                    if (isPieceHit(x, y, bullet)) {
                        println("[Barriers & Floors] Piece hit by the enemy!")
                        return true
                    }
                }
            }
        } else if (bullet.type() == "PLAYER") {
            for (y in h - 1 downTo 0) {
                for (x in 0 until w) {
                    if (isPieceHit(x, y, bullet)) {
                        println("[Barriers & Floors] Piece hit by the player!")
                        return true
                    }
                }
            }
        }

        return false
    }
}
