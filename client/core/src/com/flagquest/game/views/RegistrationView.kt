package com.flagquest.game.views

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox
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
import java.awt.SystemColor.text


class RegistrationView (gsm: GameStateManager, private val stage: Stage, listener: MainMenuRedirectionListener, authHandler: AuthHandler) {

    val controller: RegistrationController = RegistrationController(UserApiModel())

    private val backNavType = "back"
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val titleFont: BitmapFont = skin.getFont("title")
    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height
    private val buttonHeight = screenHeight / 11

    private val emailField = TextField("", skin).apply{ messageText="E-mail"}
    private val usernameField = TextField("", skin).apply{ messageText="Username"}
    private val passwordField = TextField("", skin).apply{
        messageText="Password"
        isPasswordMode = true
        setPasswordCharacter('*')
    }
    private val regBtn = TextButton("REGISTER", skin)
    private val inputFields = arrayOf(emailField, usernameField, passwordField)

    init {
        controller.redirectionListener = listener
        controller.regErrorListener = { error ->
            showError(error)
        }

        titleFont.data.setScale(1.5f)

        UIManager.addHeading(stage, "REGISTRATION", 2.8f)
        UIManager.addBackButton(stage, gsm, backNavType)

        // Read all countries
        val filename = "countries-limited.txt" //TODO: change to "countries.txt" when backend functionality implemented
        val countries = arrayOf("  Nationality") + // Show nationality as first option. TODO: Logic for not accepting that as answer.
                Gdx.files.internal(filename).readString().
                split("\n").
                map { "  $it" }. // Add padding on left
                toTypedArray()

        // Initialise countrySelectBox
        val countrySelectBox = SelectBox<String>(skin)
        countrySelectBox.setItems(*countries)

        // Position of first
        var posFirstTextField = screenHeight / 2 + 50f

        // SelectBox styling
        countrySelectBox.height = buttonHeight.toFloat()
        countrySelectBox.width = (screenWidth*80/100).toFloat()
        countrySelectBox.setPosition(screenWidth / 2 - countrySelectBox.width / 2, posFirstTextField + 30 + countrySelectBox.height)

        // Limit the number of countries displayed
        countrySelectBox.maxListCount = 10

        stage.addActor(countrySelectBox)

        // Add padding to TextField
        skin.get(TextField.TextFieldStyle::class.java).apply {
            background.leftWidth = 50f // Set left padding
        }


        for (input in inputFields) {
            input.width = (screenWidth*80/100).toFloat()
            input.height = buttonHeight.toFloat()
            input.setPosition((screenWidth - input.width) / 2, posFirstTextField)
            stage.addActor(input)
            posFirstTextField -= (buttonHeight + 30)
        }

        regBtn.setSize((screenWidth*80/100).toFloat(), buttonHeight.toFloat())
        regBtn.setPosition(screenWidth / 2 - regBtn.width / 2, posFirstTextField)
        regBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                val nationality = countrySelectBox.selected.substring(2).trim()
                controller.onRegisterClicked(authHandler, emailField.text, passwordField.text, usernameField.text, nationality)
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