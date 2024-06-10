package com.tomek20225.desktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class EnemyPlane(
    level: Int,
    private val squidImg1: Texture,
    private val squidImg2: Texture,
    private val crabImg1: Texture,
    private val crabImg2: Texture,
    private val octopusImg1: Texture,
    private val octopusImg2: Texture,
    private val ufoImg: Texture
) {
    private val rows: Int = 5
    private val cols: Int = 11
    private var invaderCount: Int = rows * cols
    private val invaderSize: Int = 32

    private var planeSpeed: Float = 4.75f + (level - 1) * 0.5f
    private var invaderSpeed: Float = planeSpeed / invaderCount
    private var invaders: Array<Array<Invader?>> = Array(rows) { arrayOfNulls<Invader?>(cols) }

    private var xBegin: Float = (Gdx.graphics.width - (cols * invaderSize)) / 2f
    private var xEnd: Float = xBegin + (invaderSize * cols)
    private var yBegin: Float = Gdx.graphics.height - (132f + invaderSize * ((level - 1) % 4) + rows * invaderSize)
    private var yEnd: Float = yBegin + (rows * invaderSize)

    private val startDirection: String = "RIGHT"
    private var currentDirection: String = startDirection

    private var ufo: UFO? = null
    private val ufoProbability: Float = 0.000324f
    private val ufoSpeed: Float = 1.5f

    init {
        for (y in 0 until rows) {
            for (x in 0 until cols) {
                val invaderX = xBegin + (x * invaderSize)
                val invaderY = yBegin + (y * invaderSize)
                invaders[y][x] = when (y) {
                    0 -> Squid(invaderX, invaderY, squidImg1, squidImg2)
                    1, 2 -> Crab(invaderX, invaderY, crabImg1, crabImg2)
                    else -> Octopus(invaderX, invaderY, octopusImg1, octopusImg2)
                }
            }
        }
    }

    private fun getFirstInvaderX(): Float {
        for (x in 0 until cols) {
            for (y in rows - 1 downTo 0) {
                if (invaders[y][x] != null) {
                    return invaders[y][x]!!.x()
                }
            }
        }
        return xBegin
    }

    private fun getLastInvaderX(): Float {
        for (x in cols - 1 downTo 0) {
            for (y in rows - 1 downTo 0) {
                if (invaders[y][x] != null) {
                    return invaders[y][x]!!.x() + invaderSize
                }
            }
        }
        return xEnd
    }

    fun getLastInvaderY(): Float {
        for (y in rows - 1 downTo 0) {
            for (x in cols - 1 downTo 0) {
                if (invaders[y][x] != null) {
                    return invaders[y][x]!!.y() + invaderSize
                }
            }
        }
        return yEnd
    }

    fun move() {
        val speed = if (currentDirection == "LEFT") -planeSpeed else planeSpeed
        for (y in 0 until rows) {
            for (x in 0 until cols) {
                invaders[y][x]?.move(currentDirection, invaderSpeed)
            }
        }
        xBegin += speed
        xEnd += speed
        if (getFirstInvaderX() <= 4) {
            currentDirection = "RIGHT"
            yBegin -= invaderSize
            yEnd -= invaderSize
            moveInvadersDown()
        } else if (getLastInvaderX() >= Gdx.graphics.width - 4) {
            currentDirection = "LEFT"
            yBegin -= invaderSize
            yEnd -= invaderSize
            moveInvadersDown()
        }
        if (ufo == null) {
            if (Math.random() <= ufoProbability) {
                ufo = UFO(ufoImg)
                println("[Game] A wild UFO appeared!")
            }
        } else {
            if (ufo!!.isOutOfBounds()) {
                ufo = null
            } else {
                ufo!!.move("RIGHT", ufoSpeed)
            }
        }
    }

    private fun moveInvadersDown() {
        for (y in 0 until rows) {
            for (x in 0 until cols) {
                invaders[y][x]?.moveDown(invaderSize)
            }
        }
    }

    fun isOnBottom(): Boolean {
        return getLastInvaderY() > Gdx.graphics.height - 88
    }

    fun isEmpty(): Boolean {
        return invaderCount == 0
    }

    fun isHit(bullet: Bullet): Int {
        var pointsGained = 0
        for (y in 0 until rows) {
            for (x in 0 until cols) {
                invaders[y][x]?.let {
                    val isHitX = (bullet.x() + bullet.width() >= it.x() && bullet.x() <= it.x() + it.width())
                    val isHitY = (bullet.y() <= it.y() + it.height() && bullet.y() >= it.y())
                    if (isHitX && isHitY) {
                        println("[Game] Invader destroyed!")
                        pointsGained = it.getPoints()
                        invaders[y][x] = null
                        invaderCount--
                        invaderSpeed = planeSpeed / invaderCount
                        return pointsGained
                    }
                }
            }
        }
        ufo?.let {
            val isUfoHitX = (bullet.x() + bullet.width() >= it.x() && bullet.x() <= it.x() + it.width())
            val isUfoHitY = (bullet.y() <= it.y() + it.height() && bullet.y() >= it.y())
            if (isUfoHitX && isUfoHitY) {
                println("[Game] UFO destroyed!")
                pointsGained = it.getPoints()
                ufo = null
            }
        }
        return pointsGained
    }

    fun show(batch: SpriteBatch) {
        if (this.isEmpty()) return

        for (y in 0 until this.rows) {
            for (x in 0 until this.cols) {
                if (this.invaders[y][x] != null) {
                    this.invaders[y][x]?.show(batch)
                }
            }
        }

        ufo?.show(batch)
    }

    fun getBottomInvaders(): Array<Invader?> {
        val bottomInvaders = Array<Invader?>(this.cols) { null }

        for (x in 0 until this.cols) {
            for (y in this.rows - 1 downTo 0) {
                if (this.invaders[y][x] != null) {
                    bottomInvaders[x] = this.invaders[y][x]
                    break
                }
            }
        }

        return bottomInvaders
    }

    fun getRandomBottomInvader(): Invader? {
        val bottomInvaders = getBottomInvaders()
        var bottomInvadersLeft = 0

        for (invader in bottomInvaders) {
            if (invader != null) {
                bottomInvadersLeft++
            }
        }

        val randomInvaderIndex = kotlin.math.floor(Math.random() * bottomInvadersLeft).toInt()
        var z = 0

        for (invader in bottomInvaders) {
            if (invader != null) {
                if (z == randomInvaderIndex && invader.y() < (Gdx.graphics.height - 102)) {
                    return invader
                }
                z++
            }
        }

        return null
    }
}
