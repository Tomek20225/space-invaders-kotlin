package com.tomek20225.desktop

class Floor(xF: Float, yF: Float, w: Int) : PieceCollection(xF, yF, w / 2, 1) {
    init {
        for (y in 0 until super.h) {
            for (x in 0 until super.w) {
                super.pieces[y][x] = true
            }
        }
    }

    fun fillGaps() {
        for (i in super.pieces[0].indices) {
            if (i % 3 == 0) {
                super.pieces[0][i] = true
            }
        }
    }
}
