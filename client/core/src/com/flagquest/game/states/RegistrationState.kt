package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.models.AuthHandler
import com.flagquest.game.utils.UIManager.addBackButton
import com.flagquest.game.utils.UIManager.addHeading

class RegistrationState(gsm: GameStateManager, private val authHandler: AuthHandler) : State(gsm) {
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val titleFont: BitmapFont = skin.getFont("title")
    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height
    private val buttonHeight = screenHeight / 11
    override val stage = Stage(ScreenViewport())


    private val emailField = TextField("", skin).apply{ messageText="  Username"}
    private val passwordField = TextField("", skin).apply{
        messageText="  Password"
        isPasswordMode = true
        setPasswordCharacter('*')
    }
    private val regBtn = TextButton("REGISTER", skin)
    private val inputFields = arrayOf(emailField, passwordField)

    init {
        titleFont.data.setScale(1.5f)

        addHeading(stage,"REGISTRATION", 2.8f)
        addBackButton(stage,gsm, backNavType)

        var posY = screenHeight / 2 + 50f
        for (input in inputFields) {
            input.width = (screenWidth*80/100).toFloat()
            input.height = buttonHeight.toFloat()
            input.setPosition((screenWidth - input.width) / 2, posY)
            stage.addActor(input)
            posY -= (buttonHeight + 30)
        }

        regBtn.setSize((screenWidth*80/100).toFloat(), buttonHeight.toFloat())
        regBtn.setPosition(screenWidth / 2 - regBtn.width / 2, posY)
        regBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                registerUser(emailField.text, passwordField.text)
            }
        })

        stage.addActor(regBtn)
    }

    private fun registerUser(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            authHandler.signUp(email, password) { success, message ->
                if (success) {
                    updateUI(success)
                } else {
                    Gdx.app.log("Registration", "Failed: $message")
                }
            }
        } else {
            Gdx.app.log("Registration", "Email or password cannot be empty")
        }
    }


    private fun updateUI(isLoggedIn: Boolean) {
        Gdx.app.postRunnable {
            if (isLoggedIn) {
                gsm.set(MainMenuState(gsm))
            } else {
                // TODO: Handle failed login, show error message
            }
        }
    }

    override fun handleInput() {
        screenWidth = screenWidth // Just placeholder code to make the code compile
    }

    override fun update(dt: Float) {
        stage.act(dt)
    }
    override fun render() {
        Gdx.gl.glClearColor(0.92f, 0.88f, 0.84f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.draw()
    }
}