package com.tomek20225.desktop

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2

class InputHandler(private val player: Player, private val bullets: PieceCollection<Bullet>, private val bulletTexture: Texture) : InputProcessor {
    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.SPACE -> {
                // Shoot a bullet
                val bullet = Bullet(bulletTexture, Vector2(player.x + player.texture.width / 2f, player.y + player.texture.height), 300f)
                bullets.add(bullet)
            }
        }
        return true
    }

    override fun keyUp(keycode: Int): Boolean = false
    override fun keyTyped(character: Char): Boolean = false
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = false
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = false
    override fun mouseMoved(screenX: Int, screenY: Int): Boolean = false
    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        TODO("Not yet implemented")
    }
}
