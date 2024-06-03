package com.tomek20225.desktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture

class UFO(
    img: Texture
) : Invader(-50f, 86f, 48, 21, img, img, 200) {

    fun isOutOfBounds(): Boolean {
        if (x() > Gdx.graphics.width) println("[Game] UFO flew away!")
        return x() > Gdx.graphics.width
    }
}
