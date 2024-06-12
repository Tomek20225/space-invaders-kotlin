package com.tomek20225.desktop

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import java.io.RandomAccessFile

class Game : ApplicationAdapter() {
    // Game objects
    private lateinit var players: Array<Player?>
    private lateinit var plane: EnemyPlane
    private lateinit var barriers: Array<Barrier>
    private lateinit var floor: Floor
    private lateinit var playerBullets: Array<Array<Bullet?>>
    private lateinit var enemyBullets: Array<Bullet?>
    private lateinit var player1Keys: BooleanArray
    private lateinit var player2Keys: BooleanArray
    private lateinit var game: Game

    // Flags
    private var playerScores = intArrayOf(0, 0)
    private var highestScore = 0
    private var level = 1
    private var playerLives = intArrayOf(3, 3)
    private var isPaused = true
    private var isStarted = false
    private var isOver = false
    private var mode: String = "SINGLEPLAYER"

    // Textures
    private lateinit var batch: SpriteBatch
    private lateinit var font: BitmapFont
    private lateinit var shapeRenderer: ShapeRenderer
    private lateinit var playerTexture: Texture
    private lateinit var playerBulletImg: Texture
    private lateinit var playerBulletImgFail: Texture
    private lateinit var playerBulletImgSuccess: Texture
    private lateinit var enemyBulletImg: Texture
    private lateinit var enemyBulletImgFail: Texture
    private lateinit var enemyBulletImgSuccess: Texture
    private lateinit var squidImg1: Texture
    private lateinit var squidImg2: Texture
    private lateinit var crabImg1: Texture
    private lateinit var crabImg2: Texture
    private lateinit var octopusImg1: Texture
    private lateinit var octopusImg2: Texture
    private lateinit var ufoImg: Texture

    override fun create() {
        // Setup renderers
        batch = SpriteBatch()
        shapeRenderer = ShapeRenderer()
        loadTextures()
        loadFont()

        try {
            highestScore = readHighestScore()
        } catch (e: Exception) {
            println(e.message)
        }

        // Initialize players key presses
        player1Keys = BooleanArray(3) { false }
        player2Keys = BooleanArray(3) { false }
        game = Game()

        // Initialize players and bullets arrays
        players = arrayOfNulls(2)
        playerBullets = arrayOf(arrayOfNulls(1), arrayOfNulls(1))
        enemyBullets = arrayOfNulls(3)

        setupLevel()

        Gdx.input.inputProcessor = object : InputAdapter() {
            override fun keyDown(keycode: Int): Boolean {
                handleKeyDown(keycode)
                return true
            }

            override fun keyUp(keycode: Int): Boolean {
                handleKeyUp(keycode)
                return true
            }
        }
    }

    override fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()

        if (isOver) {
            showGameOver()
        } else if (isStarted) {
            play(player1Keys, player2Keys)
        } else {
            showMenu()
        }

        batch.end()
    }

    private fun loadTextures() {
        val bulletTexture = Texture(Gdx.files.internal("bullet.png")).apply {
            setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        }
        playerTexture = bulletTexture
        playerBulletImg = bulletTexture
        playerBulletImgFail = bulletTexture
        playerBulletImgSuccess = bulletTexture
        enemyBulletImg = bulletTexture
        enemyBulletImgFail = bulletTexture
        enemyBulletImgSuccess = bulletTexture

        squidImg1 = Texture(Gdx.files.internal("squid1.png")).apply {
            setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        }
        squidImg2 = Texture(Gdx.files.internal("squid2.png")).apply {
            setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        }
        crabImg1 = Texture(Gdx.files.internal("crab1.png")).apply {
            setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        }
        crabImg2 = Texture(Gdx.files.internal("crab2.png")).apply {
            setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        }
        octopusImg1 = Texture(Gdx.files.internal("octopus1.png")).apply {
            setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        }
        octopusImg2 = Texture(Gdx.files.internal("octopus2.png")).apply {
            setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        }
        ufoImg = Texture(Gdx.files.internal("ufo.png")).apply {
            setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        }
    }

    private fun loadFont() {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("space_invaders.ttf"))
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter().apply {
            size = 16
            color = Color.WHITE
        }
        font = generator.generateFont(parameter)
        generator.dispose()
    }

    private fun handleKeyDown(keycode: Int) {
        if (!isPaused && isStarted && !isOver) {
            setMovement(keycode, true)
        } else if (isOver) {
            if (keycode == Input.Keys.NUM_1) {
                start("SINGLEPLAYER")
            }
        } else {
            when (keycode) {
                Input.Keys.NUM_1 -> start("SINGLEPLAYER")
                Input.Keys.NUM_2 -> start("MULTIPLAYER")
            }
        }
    }

    private fun handleKeyUp(keycode: Int) {
        if (!isPaused && isStarted) {
            setMovement(keycode, false)
        }
    }

    private fun setMovement(keycode: Int, state: Boolean) {
        when (keycode) {
            Input.Keys.A -> player1Keys[0] = state
            Input.Keys.D -> player1Keys[1] = state
            Input.Keys.W -> player1Keys[2] = state
        }
        if (isMultiplayer()) {
            when (keycode) {
                Input.Keys.LEFT -> player2Keys[0] = state
                Input.Keys.RIGHT -> player2Keys[1] = state
                Input.Keys.UP -> player2Keys[2] = state
            }
        }
    }

    private fun play(player1Keys: BooleanArray, player2Keys: BooleanArray) {
        if (players[0] != null || (isMultiplayer() && players[1] != null)) {
            players[0]?.show(batch)
            if (isMultiplayer() && players[1] != null) {
                players[1]?.show(batch)
            }
        } else {
            gameOver()
            return
        }

        if (plane.isOnBottom()) {
            gameOver()
            return
        }

        if (plane.isEmpty()) {
            nextLevel()
            return
        }

        checkPlayerBullets()
        checkEnemyBullets()
        checkBarriers()
        movePlayers(player1Keys, player2Keys)
        showHeader()
        showFooter()
        plane.show(batch)
        plane.move()
    }

    private fun setupLevel() {
        players[0] = Player(playerTexture)
        if (isMultiplayer()) {
            players[1] = Player(playerTexture)
        }

        plane = EnemyPlane(level, squidImg1, squidImg2, crabImg1, crabImg2, octopusImg1, octopusImg2, ufoImg)

        val barrierWidth = 44
        val barriersDist = (Gdx.graphics.width - (4 * barrierWidth)) / 5f
        var barrierX = barriersDist
        barriers = Array(4) {
            val barrier = Barrier(barrierX, (Gdx.graphics.height - 132).toFloat())
            barrierX += barriersDist + barrierWidth
            barrier
        }

        val floorW = 434
        val floorX = (Gdx.graphics.width - floorW) / 2f
        floor = Floor(floorX, (Gdx.graphics.height - 44).toFloat(), floorW)

        removeBullets()
        isPaused = false
    }

    private fun showHeader() {
        font.color = Color.WHITE

        font.draw(batch, "SCORE<1>", 28f, (Gdx.graphics.height - 24).toFloat())
        font.draw(batch, playerScores[0].toString(), 58f, (Gdx.graphics.height - 54).toFloat())

        font.draw(batch, "HI-SCORE", (Gdx.graphics.width / 2).toFloat() - 48f, (Gdx.graphics.height - 24).toFloat())
        font.draw(batch, highestScore.toString(), (Gdx.graphics.width / 2).toFloat() - 24f, (Gdx.graphics.height - 54).toFloat())

        font.draw(batch, "SCORE<2>", (Gdx.graphics.width - 28).toFloat() - 96f, (Gdx.graphics.height - 24).toFloat())
        if (isMultiplayer() || !isStarted) {
            font.draw(batch, playerScores[1].toString(), (Gdx.graphics.width - 58).toFloat() - 32f, (Gdx.graphics.height - 54).toFloat())
        }
    }

    private fun showFooter() {
        font.color = Color.WHITE

        val textY = (44 + 4).toFloat() - 16f

        font.draw(batch, playerLives[0].toString(), 28f, textY)
        font.draw(batch, "CREDIT  00", (Gdx.graphics.width - 28).toFloat() - 96f, textY)

        floor.fillGaps()
        floor.show(shapeRenderer)

        var xLivesBegin = (28 + 22).toFloat()
        for (i in 0 until if (playerLives[0] - 1 > 3) 3 else playerLives[0] - 1) {
            batch.draw(playerTexture, xLivesBegin + (i * 26) + (i * 4), textY, 26f, 0f)
        }

        if (isMultiplayer()) {
            xLivesBegin = 190f
            font.draw(batch, playerLives[1].toString(), 170f, textY)
            for (i in 0 until if (playerLives[1] - 1 > 3) 3 else playerLives[1] - 1) {
                batch.draw(playerTexture, xLivesBegin + (i * 26) + (i * 4), textY, 26f, 16f)
            }
        }
    }

    private fun showSimplifiedFooter() {
        font.color = Color.WHITE
        val textY = (44 + 4).toFloat()
        font.draw(batch, "CREDIT  01", (Gdx.graphics.width - 28).toFloat() - 96f, textY)
    }

    private fun showOptions() {
        font.color = Color.WHITE

        font.draw(batch, "PLAY", (Gdx.graphics.width / 2).toFloat(), 130f)
        font.draw(batch, "SPACE    INVADERS", (Gdx.graphics.width / 2).toFloat(), 176f)
        font.draw(batch, "1 PLAYER -- PRESS 1", (Gdx.graphics.width / 2).toFloat(), 238f)
        font.draw(batch, "2 PLAYERS -- PRESS 2", (Gdx.graphics.width / 2).toFloat(), 270f)
        font.draw(batch, "BY TOMEK20225", (Gdx.graphics.width / 2).toFloat(), 332f)
    }

    private fun showGameOver() {
        showHeader()

        font.draw(batch, "GAME OVER", Gdx.graphics.width / 2f, 131f)

        font.color = Color.RED
        font.draw(batch, "GAME OVER", Gdx.graphics.width / 2f, 130f)

        font.color = Color.WHITE
        font.draw(batch, "RESTART GAME -- PRESS 1", Gdx.graphics.width / 2f, 238f)
    }

    private fun removeBullets() {
        for (i in enemyBullets.indices) {
            enemyBullets[i] = null
        }

        for (i in playerBullets[0].indices) {
            playerBullets[0][i] = null
        }

        if (isMultiplayer()) {
            for (i in playerBullets[1].indices) {
                playerBullets[1][i] = null
            }
        }
    }

    private fun respawnPlayer(playerNum: Int) {
        val index = playerNum - 1
        isPaused = true

        if (playerLives[index] > 0) {
            playerLives[index]--
            players[index] = Player(playerTexture)

            println("[Game] Respawning player ${index + 1}. Lives left: ${playerLives[index]}")
            Thread.sleep(1500)

            players[index]?.show(batch)

            removeBullets()

            Thread.sleep(1500)
            isPaused = false
        } else {
            println("[Game] PLAYER $index IS DONE FOR!")
            players[index] = null
            isPaused = false
        }

        if (players.all { it == null }) {
            println("[Game] GAME OVER!")
            gameOver()
        }
    }

    private fun nextLevel() {
        isPaused = true
        level++
        playerLives[0]++

        if (isMultiplayer()) {
            playerLives[1]++
        }

        println("[Game] ADVANCING TO LEVEL $level")
        Thread.sleep(2000)

        setupLevel()
    }

    private fun gameOver() {
        Thread.sleep(2000)
        isOver = true
        isPaused = false
        isStarted = false
    }

    private fun start(mode: String) {
        this.mode = mode
        this.isStarted = true

        // Reinitialize players and bullets arrays
        players = arrayOfNulls(2)
        playerBullets = arrayOf(arrayOfNulls(1), arrayOfNulls(1))
        enemyBullets = arrayOfNulls(3)

        loadTextures()
        loadFont()

        setupLevel()
    }

    private fun showMenu() {
        showHeader()
        showOptions()
        showSimplifiedFooter()
    }

    private fun isMultiplayer(): Boolean {
        return mode == "MULTIPLAYER"
    }

    @Throws(Exception::class)
    private fun saveHighestScore() {
        var raf: RandomAccessFile? = null
        try {
            raf = RandomAccessFile("${System.getProperty("user.dir")}/space_invaders.dat", "rw")
            raf.seek(0)
            raf.writeInt(highestScore)
        } catch (e: Exception) {
            println(e.message)
        } finally {
            raf?.close()
        }
    }

    @Throws(Exception::class)
    private fun readHighestScore(): Int {
        var raf: RandomAccessFile? = null
        var result = 0
        try {
            raf = RandomAccessFile("${System.getProperty("user.dir")}/space_invaders.dat", "r")
            raf.seek(0)
            result = raf.readInt()
        } catch (e: Exception) {
            println(e.message)
        } finally {
            raf?.close()
        }
        return result
    }

    private fun checkPlayerBullets() {
        val max = if (isMultiplayer()) 1 else 0

        for (p in 0..max) {
            for (i in playerBullets[p].indices) {
                if (playerBullets[p][i] != null) {
                    for (b in barriers.indices) {
                        if (barriers[b].isHit(playerBullets[p][i]!!)) {
                            playerBullets[p][i]?.explodeFail(batch)
                            playerBullets[p][i] = null
                            break
                        }
                    }

                    if (playerBullets[p][i] != null) {
                        for (b in enemyBullets.indices) {
                            if (enemyBullets[b] != null && playerBullets[p][i]?.collidesWith(enemyBullets[b]!!) == true) {
                                playerBullets[p][i]?.explodeFail(batch, true)
                                playerBullets[p][i] = null

                                if (Math.random() > 0.2) {
                                    enemyBullets[b] = null
                                }

                                break
                            }
                        }
                    } else {
                        continue
                    }

                    if (playerBullets[p][i] != null) {
                        val pointsGained = plane.isHit(playerBullets[p][i]!!)
                        if (pointsGained != 0) {
                            playerScores[p] += pointsGained
                            println("[Game] PLAYER ${p + 1} SCORE: ${playerScores[p]}")

                            if (playerScores[p] > highestScore) {
                                highestScore = playerScores[p]
                                try {
                                    saveHighestScore()
                                } catch (e: Exception) {
                                    println(e.message)
                                }
                            }

                            playerBullets[p][i]?.explode(batch)
                            playerBullets[p][i] = null
                            continue
                        }
                    } else {
                        continue
                    }

                    if (playerBullets[p][i] != null && playerBullets[p][i]?.isOutOfBounds() == true) {
                        playerBullets[p][i]?.explodeFail(batch)
                        playerBullets[p][i] = null
                        continue
                    }

                    if (playerBullets[p][i] != null) {
                        playerBullets[p][i]?.show(batch)
                        playerBullets[p][i]?.move()
                    }
                }
            }
        }
    }

    private fun checkEnemyBullets() {
        for (i in enemyBullets.indices) {
            if (enemyBullets[i] != null) {
                for (b in barriers.indices) {
                    if (barriers[b].isHit(enemyBullets[i]!!)) {
                        enemyBullets[i]?.explodeFail(batch)
                        enemyBullets[i] = null
                        break
                    }
                }

                val max = if (isMultiplayer()) 1 else 0
                for (p in 0..max) {
                    if (players[p] != null) {
                        if (enemyBullets[i] != null && players[p]?.isHit(enemyBullets[i]!!) == true) {
                            isPaused = true
                            enemyBullets[i]?.explode(batch)
                            enemyBullets[i] = null
                            respawnPlayer(p + 1)
                            continue
                        }
                    } else {
                        continue
                    }
                }

                if (enemyBullets[i] != null && floor.isHit(enemyBullets[i]!!)) {
                    enemyBullets[i]?.explodeFail(batch)
                    enemyBullets[i] = null
                    continue
                }

                if (enemyBullets[i] != null && enemyBullets[i]?.isOutOfBounds() == true) {
                    enemyBullets[i]?.explodeFail(batch)
                    enemyBullets[i] = null
                    continue
                }

                if (enemyBullets[i] != null) {
                    enemyBullets[i]?.show(batch)
                    enemyBullets[i]?.move()
                }
            } else {
                if (Math.random() <= 0.005) {
                    shootPlayer()
                }
            }
        }
    }

    private fun checkBarriers() {
        val invadersBottomY = plane.getLastInvaderY()
        for (i in barriers.indices) {
            if (invadersBottomY != -1f && invadersBottomY >= Gdx.graphics.height - 102) {
                barriers[i].isDestroyedByInvaders(plane.getBottomInvaders())
            }
            barriers[i].show(shapeRenderer)
        }
    }

    private fun movePlayers(player1Keys: BooleanArray, player2Keys: BooleanArray) {
        if (!isPaused) {
            if (player1Keys[0]) {
                movePlayer(1, "LEFT")
            } else if (player1Keys[1]) {
                movePlayer(1, "RIGHT")
            } else if (player1Keys[2]) {
                shootEnemies(1)
            }

            if (isMultiplayer()) {
                if (player2Keys[0]) {
                    movePlayer(2, "LEFT")
                } else if (player2Keys[1]) {
                    movePlayer(2, "RIGHT")
                } else if (player2Keys[2]) {
                    shootEnemies(2)
                }
            }
        }
    }

    private fun movePlayer(playerNum: Int, dir: String) {
        val index = playerNum - 1
        if (players[index] != null) {
            players[index]?.move(dir)
        }
    }

    private fun shootPlayer() {
        for (i in enemyBullets.indices) {
            if (enemyBullets[i] == null) {
                println("[Enemy] Shooting the player!")
                val randomInvader = plane.getRandomBottomInvader()
                if (randomInvader != null) {
                    enemyBullets[i] = Bullet(
                        randomInvader.x() + 8,
                        randomInvader.y() - 5,
                        "ENEMY",
                        enemyBulletImg,
                        enemyBulletImgFail,
                        enemyBulletImgSuccess
                    )
                }
                return
            }
        }
    }

    private fun shootEnemies(playerNum: Int) {
        val index = playerNum - 1
        if (players[index] != null) {
            for (i in playerBullets[index].indices) {
                if (playerBullets[index][i] == null) {
                    playerBullets[index][i] = Bullet(
                        players[index]!!.x(),
                        players[index]!!.y(),
                        "PLAYER",
                        playerBulletImg,
                        playerBulletImgFail,
                        playerBulletImgSuccess
                    )
                    break
                }
            }
        }
    }
}
