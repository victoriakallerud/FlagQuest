package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.utils.ButtonClickListener

class JoinGameState(gsm: GameStateManager) : State(gsm) {
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val titleFont: BitmapFont = skin.getFont("title")
    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height
    private val buttonHeight = screenHeight / 11
    private var pos: Float = ((screenHeight / 2) + 50).toFloat()
    private val stage = Stage(ScreenViewport())

    private val heading = Label("JOIN GAME\nLOBBY", skin)
    private val codeInput = TextField("", skin).apply{ messageText="  GAME CODE"}
    private val codeBtn = TextButton("JOIN WITH CODE", skin)
    private val randomBtn = TextButton("JOIN RANDOM GAME", skin)

    private val btns = arrayOf(
        codeBtn to lazy { GameLobbyState(gsm, isAdmin = false, "14d29155-82ef-4d11-9c36-7214d1a8e4b7") }, //TODO: Add backend logic
        randomBtn to lazy { GameLobbyState(gsm, isAdmin = false, "14d29155-82ef-4d11-9c36-7214d1a8e4b7") } ) //TODO: Add backend logic
    private var counter: Int = 1

    init {
        Gdx.input.inputProcessor = stage

        heading.setStyle(Label.LabelStyle(titleFont, heading.style.fontColor))
        heading.setFontScale(2.8f)
        heading.setAlignment(Align.center)
        heading.pack()
        heading.setPosition((screenWidth - heading.prefWidth) / 2, screenHeight - 500f)
        stage.addActor(heading)

        codeInput.width = (screenWidth*80/100).toFloat()
        codeInput.height = buttonHeight.toFloat()
        codeInput.setPosition(screenWidth / 2 - codeInput.width / 2, pos)
        stage.addActor(codeInput)

        titleFont.data.setScale(1.5f)
        for (btn in btns) {
            btn.first.width = (screenWidth*80/100).toFloat()
            btn.first.height = buttonHeight.toFloat()
            btn.first.setPosition(screenWidth / 2 - btn.first.width / 2, pos - (buttonHeight + 30) * counter)
            btn.first.addListener(ButtonClickListener(gsm, btn.second))
            stage.addActor(btn.first)
            counter++
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