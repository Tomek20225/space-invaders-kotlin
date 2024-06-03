package com.tomek20225.desktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture

class EnemyPlane(level: Int) {
    private val rows: Int = 5
    private val cols: Int = 11
    private var invaderCount: Int = rows * cols
    private val invaderSize: Int = 32

    private var planeSpeed: Float = 4.75f + (level - 1) * 0.5f
    private var invaderSpeed: Float = planeSpeed / invaderCount
    private var invaders: Array<Array<Invader?>> = Array(rows) { arrayOfNulls<Invader?>(cols) }

    private var xBegin: Float = (Gdx.graphics.width - (cols * invaderSize)) / 2f
    private var xEnd: Float = xBegin + (invaderSize * cols)
    private var yBegin: Float = 132f + invaderSize * ((level - 1) % 4)
    private var yEnd: Float = yBegin + (rows * invaderSize)

    private val startDirection: String = "RIGHT"
    private var currentDirection: String = startDirection

    private var ufo: UFO? = null
    private val ufoProbability: Float = 0.000324f
    private val ufoSpeed: Float = 1.5f

    private val squidImg1 = Texture("squid1.png")
    private val squidImg2 = Texture("squid2.png")
    private val crabImg1 = Texture("crab1.png")
    private val crabImg2 = Texture("crab2.png")
    private val octopusImg1 = Texture("octopus1.png")
    private val octopusImg2 = Texture("octopus2.png")
    private val ufoImg = Texture("ufo1.png")

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

    private fun getLastInvaderY(): Float {
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
            yBegin += invaderSize
            yEnd += invaderSize
        } else if (getLastInvaderX() >= Gdx.graphics.width - 4) {
            currentDirection = "LEFT"
            yBegin += invaderSize
            yEnd += invaderSize
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
}
