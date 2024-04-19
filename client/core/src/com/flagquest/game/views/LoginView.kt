package com.flagquest.game.views

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.flagquest.game.controllers.LoginController
import com.flagquest.game.models.AuthHandler
import com.flagquest.game.models.UserApiModel
import com.flagquest.game.navigation.MainMenuRedirectionListener
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.utils.UIManager
val backNavType = "back"

class LoginView(gsm: GameStateManager, private val stage: Stage, listener: MainMenuRedirectionListener, authHandler: AuthHandler) {
    val controller: LoginController = LoginController(UserApiModel())

    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val titleFont: BitmapFont = skin.getFont("title")
    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height
    private val buttonHeight = screenHeight / 11

    private val emailField = TextField("", skin).apply{ messageText="E-mail"}
    private val passwordField = TextField("", skin).apply{
        messageText = "Password"
        isPasswordMode = true
        setPasswordCharacter('*')
    }

    private val loginBtn = TextButton("LOGIN", skin)
    private val inputFields = arrayOf(emailField, passwordField)


    init {
        controller.redirectionListener = listener
        controller.loginErrorListener = { error ->
            showError(error)
        }

        titleFont.data.setScale(1.5f)

        UIManager.addHeading(stage, "LOGIN", 2.8f)

        var posY = screenHeight / 2 + 50f

        // Add padding to TextField
        skin.get(TextField.TextFieldStyle::class.java).apply {
            background.leftWidth = 50f // Set left padding
        }

        for (input in inputFields) {
            input.width = (screenWidth*80/100).toFloat()
            input.height = buttonHeight.toFloat()
            input.setPosition((screenWidth - input.width) / 2, posY)
            stage.addActor(input)
            posY -= (buttonHeight + 30)
        }

        loginBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                controller.onLoginClicked(authHandler, emailField.text, passwordField.text)
            }
        })
        loginBtn.setSize((screenWidth*80/100).toFloat(), buttonHeight.toFloat())
        loginBtn.setPosition(screenWidth / 2 - loginBtn.width / 2, posY)
        stage.addActor(loginBtn)

        UIManager.addBackButton(stage, gsm, backNavType)
    }

    fun showError(error: String) {
        UIManager.addError(stage, error)
    }

    fun render() {
        stage.draw()
    }

    fun update(dt: Float) {
        stage.act(dt)
    }
}