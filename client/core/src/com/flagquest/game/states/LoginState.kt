package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.utils.ButtonClickListener
import com.flagquest.game.utils.UIManager.addBackButton
import com.flagquest.game.utils.UIManager.addHeading
import com.flagquest.game.utils.UIManager.addTextButton

class LoginState(gsm: GameStateManager) : State(gsm) {
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val textFieldStyle: TextField.TextFieldStyle = skin.get(TextField.TextFieldStyle::class.java)
    private val titleFont: BitmapFont = skin.getFont("title")
    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height
    private val buttonHeight = screenHeight / 11
    private var pos: Float = ((screenHeight / 2) + 50).toFloat()
    override val stage = Stage(ScreenViewport())

    private val usernameField = TextField("", skin).apply{ messageText="  Username"}
    private val passwordField = TextField("", skin).apply{
        messageText="  Password"
        isPasswordMode=true
        setPasswordCharacter('*')
    }
    val loginBtn = TextButton("LOGIN", skin) to lazy {MainMenuState(gsm)}
    val buttonY = pos - (buttonHeight + 30) * 2

    init {
        textFieldStyle.font.data.setScale(5f)
        titleFont.data.setScale(1.5f)

        addHeading(stage,"LOGIN", 2.8f)

        usernameField.width = (screenWidth*80/100).toFloat()
        usernameField.height = buttonHeight.toFloat()
        usernameField.setPosition(screenWidth / 2 - usernameField.width / 2, pos)
        stage.addActor(usernameField)

        passwordField.width = (screenWidth*80/100).toFloat()
        passwordField.height = buttonHeight.toFloat()
        passwordField.setPosition(screenWidth / 2 - passwordField.width / 2, pos - buttonHeight - 30)
        stage.addActor(passwordField)

        addTextButton(stage,gsm, loginBtn,buttonY)
        addBackButton(stage,gsm, backNavType)
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