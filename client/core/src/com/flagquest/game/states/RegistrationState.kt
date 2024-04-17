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

class RegistrationState(gsm: GameStateManager) : State(gsm) {
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val textFieldStyle: TextField.TextFieldStyle = skin.get(TextField.TextFieldStyle::class.java)
    private val titleFont: BitmapFont = skin.getFont("title")
    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height
    private val buttonHeight = screenHeight / 11
    private var pos: Float = ((screenHeight / 2) + 50).toFloat()
    override val stage = Stage(ScreenViewport())

    private val nameField = TextField("", skin).apply{ messageText="  Name"}
    private val usernameField = TextField("", skin).apply{ messageText="  Username"}
    private val passwordField = TextField("", skin).apply{
        messageText="  Password"
        isPasswordMode=true
        setPasswordCharacter('*')
    }
    private val regBtn = TextButton("REGISTER", skin)
    private val inputFields = arrayOf(nameField, usernameField, passwordField)
    private var counter: Int = 0

    init {
        textFieldStyle.font.data.setScale(5f)
        titleFont.data.setScale(1.5f)

        addHeading(stage,"REGISTRATION", 2.8f)
        addBackButton(stage,gsm, backNavType)

        for (input in inputFields) {
            input.width = (screenWidth*80/100).toFloat()
            input.height = buttonHeight.toFloat()
            input.setPosition(screenWidth / 2 - input.width / 2, pos - (buttonHeight + 30) * counter)
            stage.addActor(input)
            counter++
        }

        // Keep this in as additional logic for navigation required.
        regBtn.setSize((screenWidth*80/100).toFloat(), buttonHeight.toFloat())
        regBtn.setPosition(screenWidth / 2 - regBtn.width / 2, pos - (buttonHeight + 30) * 3)
        regBtn.addListener(ButtonClickListener(gsm, lazy { MainMenuState(gsm) }))
        stage.addActor(regBtn)
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