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
import com.flagquest.game.utils.UIManager.addHeading
import com.flagquest.game.utils.UIManager.addTextButtons

class LoginMenuState(gsm: GameStateManager) : State(gsm) {
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    val titleFont: BitmapFont = skin.getFont("title")
    var screenWidth = Gdx.graphics.width
    val screenHeight = Gdx.graphics.height
    var pos: Float = ((screenHeight / 2) + 50).toFloat()
    override val stage = Stage(ScreenViewport())

    val loginBtn = TextButton("LOGIN", skin)
    val registerBtn = TextButton("REGISTER", skin)
    val buttons = arrayOf(Pair(loginBtn, lazy { LoginState(gsm) }),Pair(registerBtn, lazy { RegistrationState(gsm) }))

    init {
        titleFont.data.setScale(1.5f)
        addHeading(stage,"FLAGQUEST",3.5f)
        addTextButtons(stage,gsm,buttons,pos)
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