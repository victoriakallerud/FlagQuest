package com.flagquest.game.views

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.flagquest.game.controllers.RegistrationController
import com.flagquest.game.models.AuthHandler
import com.flagquest.game.models.UserApiModel
import com.flagquest.game.navigation.MainMenuRedirectionListener
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.utils.UIManager

class RegistrationView (gsm: GameStateManager, private val stage: Stage, listener: MainMenuRedirectionListener, authHandler: AuthHandler) {

    val controller: RegistrationController = RegistrationController(UserApiModel())

    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val titleFont: BitmapFont = skin.getFont("title")
    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height
    private val buttonHeight = screenHeight / 11

    private val emailField = TextField("", skin).apply{ messageText="  Username"}
    private val passwordField = TextField("", skin).apply{
        messageText="  Password"
        isPasswordMode = true
        setPasswordCharacter('*')
    }
    private val regBtn = TextButton("REGISTER", skin)
    private val inputFields = arrayOf(emailField, passwordField)

    init {
        controller.redirectionListener = listener
        controller.regErrorListener = { error ->
            showError(error)
        }

        titleFont.data.setScale(1.5f)

        UIManager.addHeading(stage, "REGISTRATION", 2.8f)
        UIManager.addBackButton(stage, gsm, backNavType)

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
                controller.onRegisterClicked(authHandler, emailField.text, passwordField.text, "Alf Inge Wang")
            }
        })
        stage.addActor(regBtn)
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