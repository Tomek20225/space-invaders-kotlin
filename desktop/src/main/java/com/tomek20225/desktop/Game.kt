package com.tomek20225.desktop

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2

class Game : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var assetManager: AssetManager
    private lateinit var playerTexture: Texture
    private lateinit var bulletTexture: Texture
    private lateinit var enemyTexture: Texture
    private lateinit var player: Player
    private lateinit var inputHandler: InputHandler
    private lateinit var bullets: Bullet
    private lateinit var enemies: Invader

    override fun create() {
        batch = SpriteBatch()
        assetManager = AssetManager()

        assetManager.load("player.png", Texture::class.java)
        assetManager.load("bullet.png", Texture::class.java)
        assetManager.load("enemy.png", Texture::class.java)
        assetManager.finishLoading()

        val playerTexture = assetManager.get<Texture>("player.png")
        val bulletTexture = assetManager.get<Texture>("bullet.png")
        val enemyTexture = assetManager.get<Texture>("enemy.png")

        player = Player(playerTexture)
        bullets = mutableListOf<Bullet>()
        enemies = mutableListOf<Invader>()

        enemies.add(Crab(enemyTexture, Vector2(100f, 400f), 50f))
        enemies.add(Octopus(enemyTexture, Vector2(200f, 400f), 50f))
        enemies.add(Squid(enemyTexture, Vector2(300f, 400f), 50f))

        inputHandler = InputHandler(player, bullets, bulletTexture)
        Gdx.input.inputProcessor = inputHandler
    }

    override fun render() {
        val delta = Gdx.graphics.deltaTime

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        player.update(delta)
        bullets.update(delta)
        enemies.update(delta)

        val bulletIterator = bullets.pieces.iterator()
        while (bulletIterator.hasNext()) {
            val bullet = bulletIterator.next()
            val enemyIterator = enemies.pieces.iterator()
            while (enemyIterator.hasNext()) {
                val enemy = enemyIterator.next()
                if (bullet.bounds.overlaps(enemy.bounds)) {
                    bulletIterator.remove()
                    enemyIterator.remove()
                    break
                }
            }
        }

        batch.begin()
        player.draw(batch)
        bullets.draw(batch)
        enemies.draw(batch)
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        assetManager.dispose()
        playerTexture.dispose()
        bulletTexture.dispose()
        enemyTexture.dispose()
    }
}
