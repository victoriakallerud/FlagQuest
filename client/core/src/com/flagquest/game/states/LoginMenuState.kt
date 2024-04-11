package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.utils.ButtonClickListener

class LoginMenuState(gsm: GameStateManager) : State(gsm) {
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    val titleFont: BitmapFont = skin.getFont("title")
    var screenWidth = Gdx.graphics.width
    val screenHeight = Gdx.graphics.height
    val buttonHeight = screenHeight / 11
    var pos: Float = ((screenHeight / 2) + 50).toFloat()
    val stage = Stage(ScreenViewport())

    val heading = Label("FLAGQUEST", skin)
    val loginBtn = TextButton("LOGIN", skin)
    val registerBtn = TextButton("REGISTER", skin)
    val buttons = arrayOf(Pair(loginBtn, lazy { LoginState(gsm) }),Pair(registerBtn, lazy { RegistrationState(gsm) }))

    init {
        Gdx.input.inputProcessor = stage
        heading.setStyle(Label.LabelStyle(titleFont, heading.style.fontColor))
        heading.setFontScale(3.5f)
        heading.pack()
        heading.setPosition((screenWidth - heading.prefWidth) / 2, screenHeight - 500f)
        stage.addActor(heading)
        titleFont.data.setScale(1.5f)
        for (button in buttons) {
            button.first.setSize((screenWidth*80/100).toFloat(), buttonHeight.toFloat())
            button.first.setPosition(screenWidth / 2 - button.first.width / 2, pos)
           button.first.addListener(ButtonClickListener(gsm,button.second))
            stage.addActor(button.first)
            pos -= button.first.height + 30
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