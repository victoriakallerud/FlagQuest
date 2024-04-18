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

class LoginState(gsm: GameStateManager, private val authHandler: AuthHandler) : State(gsm) {
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val titleFont: BitmapFont = skin.getFont("title")
    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height
    private val buttonHeight = screenHeight / 11
    override val stage = Stage(ScreenViewport())

    private val emailField = TextField("", skin).apply{ messageText="  E-mail"}
    private val passwordField = TextField("", skin).apply{
        messageText = "  Password"
        isPasswordMode = true
        setPasswordCharacter('*')
    }

    private val loginBtn = TextButton("LOGIN", skin) to lazy {loginUser(emailField.text, passwordField.text)}
    private val inputFields = arrayOf(emailField, passwordField)

    init {
        titleFont.data.setScale(1.5f)

        addHeading(stage,"LOGIN", 2.8f)

        var posY = screenHeight / 2 + 50f

        for (input in inputFields) {
            input.width = (screenWidth*80/100).toFloat()
            input.height = buttonHeight.toFloat()
            input.setPosition((screenWidth - input.width) / 2, posY)
            stage.addActor(input)
            posY -= (buttonHeight + 30)
        }


        loginBtn.first.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                loginUser(emailField.text, passwordField.text)
            }
        })
        loginBtn.first.setSize((screenWidth*80/100).toFloat(), buttonHeight.toFloat())
        loginBtn.first.setPosition(screenWidth / 2 - loginBtn.first.width / 2, posY)
        stage.addActor(loginBtn.first)

        addBackButton(stage,gsm, backNavType)
    }


    private fun loginUser(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            Gdx.app.log("LoginState", "Email or password cannot be empty")
            return
        }
        Gdx.app.log("LoginState", "Attempting to login with email: $email")
        authHandler.signIn(email, password) { success, message ->
            if (success) {
                Gdx.app.log("LoginState", "Login successful")
                updateUI(true)
            } else {
                Gdx.app.log("LoginState", "Login failed: $message")
                updateUI(false)
            }
        }
    }

    private fun updateUI(isLoggedIn: Boolean) {
        Gdx.app.postRunnable {
            if (isLoggedIn) {
                gsm.set(MainMenuState(gsm))
            } else {
                // TODO: Handle failed login. Show error message
            }
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