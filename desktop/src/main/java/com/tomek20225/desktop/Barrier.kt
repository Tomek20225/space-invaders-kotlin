package com.tomek20225.desktop

class Barrier(xC: Float, yC: Float) : PieceCollection(xC, yC, 22, 16) {
    init {
        for (y in 0 until super.h) {
            for (x in 0 until super.w) {
                super.pieces[y][x] = when {
                    y == 0 && (x < 4 || x > super.w - 5) -> false
                    y == 1 && (x < 3 || x > super.w - 4) -> false
                    y == 2 && (x < 2 || x > super.w - 3) -> false
                    y == 3 && (x < 1 || x > super.w - 2) -> false
                    y == 12 && (x > 6 && x < super.w - 7) -> false
                    y == 13 && (x > 5 && x < super.w - 6) -> false
                    y >= 14 && (x > 4 && x < super.w - 5) -> false
                    else -> true
                }
            }
        }
    }

    fun isDestroyedByInvaders(bottomInvaders: Array<Invader?>): Boolean {
        for (invader in bottomInvaders) {
            if (invader != null) {
                if (this.isHit(invader)) {
                    return true
                }
            }
        }
        return false
    }

    fun isDestroyedByInvaders(invaders: Array<Array<Invader?>>): Boolean {
        for (row in invaders) {
            for (invader in row) {
                if (invader != null) {
                    if (this.isHit(invader)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun isPieceHit(x: Int, y: Int, invader: Invader): Boolean {
        if (super.pieces[y][x]) {
            val pieceX = super.x + (x * super.pxScale)
            val pieceY = super.y + (y * super.pxScale)
            val isPieceHitX = ((invader.x() + invader.width()) >= pieceX && invader.x() <= (pieceX + super.pxScale))
            val isPieceHitY = (invader.y() <= (pieceY + super.pxScale * 10) && (invader.y() + invader.height()) >= pieceY)

            if (isPieceHitX && isPieceHitY) {
                super.pieces[y][x] = false
                return true
            }
        }
        return false
    }

    fun isHit(invader: Invader): Boolean {
        if (this.isDestroyed()) {
            return false
        }

        var wasHit = false

        for (y in 0 until super.h) {
            for (x in 0 until super.w) {
                if (this.isPieceHit(x, y, invader)) {
                    println("[Barriers] Invader collided with a barrier!")
                    wasHit = true
                }
            }
        }

        return wasHit
    }
}
