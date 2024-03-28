package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.viewport.ScreenViewport

class MainMenuState(gsm: GameStateManager) : State(gsm) {
    val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    val titleFont = skin.getFont("title")
    var screenWidth = Gdx.graphics.width
    val screenHeight = Gdx.graphics.height
    val buttonHeight = screenHeight / 11
    var pos: Float = ((screenHeight / 2) + 50).toFloat()
    val stage = Stage(ScreenViewport())

    val heading = Label("FLAGQUEST", skin)
    val createBtn = TextButton("CREATE GAME", skin)
    val joinBtn = TextButton("JOIN GAME", skin)
    val trainingBtn = TextButton("TRAINING MODE", skin)
    val highscoreBtn = TextButton("HIGHSCORE BOARD", skin)
    val friendsBtn = TextButton("MANAGE FRIENDS", skin)
    val buttons = arrayOf(createBtn, joinBtn, trainingBtn, highscoreBtn, friendsBtn)

    init {
        Gdx.input.inputProcessor = stage
        heading.setStyle(Label.LabelStyle(titleFont, heading.style.fontColor))
        heading.setFontScale(3.5f)
        heading.setPosition(100f, screenHeight - 500f)
        stage.addActor(heading)
        titleFont.data.setScale(1.5f)
        for (button in buttons) {
            button.setSize((screenWidth*80/100).toFloat(), buttonHeight.toFloat())
            button.setPosition(screenWidth / 2 - button.width / 2, pos)
            stage.addActor(button)
            pos -= button.height + 30
        }
    }

    override fun handleInput() {
        screenWidth = screenWidth // Just placeholder code to make the code compile
    }
    override fun update(dt: Float) {
        handleInput()
        stage.act(dt)
    }
    override fun render() {
        Gdx.gl.glClearColor(0.92f, 0.88f, 0.84f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.draw()
    }
}