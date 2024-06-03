package com.tomek20225.desktop

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator

class Game : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var font: BitmapFont
    private lateinit var playerTexture: Texture
    private lateinit var player: Player
    private lateinit var enemyPlane: EnemyPlane
    private lateinit var barriers: Array<Barrier>
    private lateinit var floor: Floor
    private var playerBullets = Array(2) { arrayOfNulls<Bullet>(1) }
    private var enemyBullets = arrayOfNulls<Bullet>(3)
    private var playerScores = intArrayOf(0, 0)
    private var highestScore = 0
    private var level = 1
    private var playerLives = intArrayOf(3, 3)
    private var isPaused = true
    private var isStarted = false
    private var isOver = false
    private var mode = "SINGLEPLAYER"

    override fun create() {
        batch = SpriteBatch()
        playerTexture = Texture("player.png")

        // Load font
        val generator = FreeTypeFontGenerator(Gdx.files.internal("space_invaders.ttf"))
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.size = 16
        font = generator.generateFont(parameter)
        generator.dispose()

        try {
            highestScore = readHighestScore()
        } catch (e: Exception) {
            println(e.message)
        }

        setupLevel()
    }

    override fun render() {
        if (isOver) return

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()

        if (isStarted) {
            if (playerLives[0] > 0) {
                player.show(batch)
            }
            enemyPlane.move()
            enemyPlane.show(batch)
            // Show other game objects and handle interactions
            // ...
        } else {
            showStartScreen()
        }

        batch.end()

        handleInput()
        checkCollisions()
        updateGameState()
    }

    private fun setupLevel() {
        player = Player(playerTexture)
        if (mode == "MULTIPLAYER") {
            // Initialize second player here
        }

        enemyPlane = EnemyPlane(level)

        val barrierWidth = 44
        val barriersDist = (Gdx.graphics.width - (4 * barrierWidth)) / 5f
        var barrierX = barriersDist
        barriers = Array(4) {
            Barrier(barrierX, (Gdx.graphics.height - 132).toFloat())
            barrierX += barriersDist + barrierWidth
        }

        val floorWidth = 434
        val floorX = (Gdx.graphics.width - floorWidth) / 2f
        floor = Floor(floorX, (Gdx.graphics.height - 44).toFloat(), floorWidth)

        removeBullets()
        isPaused = false
    }

    private fun showHeader() {
        batch.begin()
        font.draw(batch, "SCORE<1>", 28f, Gdx.graphics.height - 24f)
        font.draw(batch, playerScores[0].toString(), 58f, Gdx.graphics.height - 54f)
        font.draw(batch, "HI-SCORE", (Gdx.graphics.width / 2).toFloat(), Gdx.graphics.height - 24f)
        font.draw(batch, highestScore.toString(), (Gdx.graphics.width / 2).toFloat(), Gdx.graphics.height - 54f)
        font.draw(batch, "SCORE<2>", (Gdx.graphics.width - 28).toFloat(), Gdx.graphics.height - 24f)
        if (mode == "MULTIPLAYER" || !isStarted) {
            font.draw(batch, playerScores[1].toString(), (Gdx.graphics.width - 58).toFloat(), Gdx.graphics.height - 54f)
        }
        batch.end()
    }

    private fun showFooter() {
        batch.begin()
        font.draw(batch, playerLives[0].toString(), 28f, (Gdx.graphics.height - 44 + 4).toFloat())
        font.draw(batch, "CREDIT  00", (Gdx.graphics.width - 28).toFloat(), (Gdx.graphics.height - 44 + 4).toFloat())

        floor.fillGaps()
        floor.show(batch)

        val textY = (Gdx.graphics.height - 44 + 4).toFloat()
        var xLivesBegin = 28 + 22
        for (i in 0 until (if (playerLives[0] - 1 > 3) 3 else playerLives[0] - 1)) {
            batch.draw(playerTexture, xLivesBegin + (i * 26) + (i * 4), textY, 26f, 16f)
        }

        if (mode == "MULTIPLAYER") {
            xLivesBegin = 190
            font.draw(batch, playerLives[1].toString(), 170f, textY)
            for (i in 0 until (if (playerLives[1] - 1 > 3) 3 else playerLives[1] - 1)) {
                batch.draw(playerTexture, xLivesBegin + (i * 26) + (i * 4), textY, 26f, 16f)
            }
        }
        batch.end()
    }

    private fun handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.move("LEFT")
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.move("RIGHT")
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            shootPlayer()
        }
        // Handle other inputs
        // ...
    }

    private fun checkCollisions() {
        // Check collisions between bullets and invaders
        // ...
    }

    private fun updateGameState() {
        if (enemyPlane.isOnBottom()) {
            gameOver()
        }
        if (enemyPlane.isEmpty()) {
            nextLevel()
        }
    }

    private fun showStartScreen() {
        // Show start screen
    }

    private fun shootPlayer() {
        // Implement shooting logic
    }

    private fun gameOver() {
        isOver = true
        // Handle game over state
    }

    private fun nextLevel() {
        level++
        setupLevel()
    }

    override fun dispose() {
        batch.dispose()
        font.dispose()
        playerTexture.dispose()
        // Dispose other resources
    }
}
