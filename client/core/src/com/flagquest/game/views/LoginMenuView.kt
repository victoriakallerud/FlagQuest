package com.flagquest.game.views

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.flagquest.game.models.AuthHandler
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.utils.UIManager

class LoginMenuView(gsm: GameStateManager, private val stage: Stage, private val authHandler: AuthHandler) {
    private val skin: Skin = UIManager.skin
    val titleFont: BitmapFont = skin.getFont("title")
    var screenWidth = Gdx.graphics.width
    val screenHeight = Gdx.graphics.height
    var pos: Float = ((screenHeight / 2) + 50).toFloat()
    val buttons = arrayOf(
        TextButton("LOGIN", skin),
        TextButton("REGISTER", skin)
    )

    init {
        titleFont.data.setScale(1.5f)
        UIManager.addHeading(stage, "FLAGQUEST", 3.5f)
        layoutButtons()
    }

    private fun layoutButtons() {
        // Set button font scale
        val buttonFont = UIManager.titleFont
        buttonFont.data.setScale(UIManager.buttonTextScale)

        buttons.forEach { button ->
            // Set and apply button style
            val buttonStyle = TextButton.TextButtonStyle(button.style)
            buttonStyle.font = buttonFont
            button.style = buttonStyle

            // Set button size and position
            button.setSize(UIManager.elementWidth.toFloat(), UIManager.elementHeight.toFloat())
            button.setPosition((UIManager.screenWidth - UIManager.elementWidth) / 2f, pos)

            stage.addActor(button)
            pos -= (UIManager.elementHeight + UIManager.elementSpacing)
        }
    }
}