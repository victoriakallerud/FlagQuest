package com.flagquest.game.views

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.flagquest.game.utils.UIManager

class LoginMenuView(private val stage: Stage) {
    private val skin: Skin = UIManager.skin
    private val titleFont: BitmapFont = UIManager.titleFont
    private var pos: Float = ((UIManager.screenHeight / 2) + 50).toFloat()
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
        val buttonFont = UIManager.titleFont
        buttonFont.data.setScale(UIManager.buttonTextScale)

        buttons.forEach { button ->
            val buttonStyle = TextButton.TextButtonStyle(button.style)
            buttonStyle.font = buttonFont
            button.style = buttonStyle

            button.setSize(UIManager.elementWidth.toFloat(), UIManager.elementHeight.toFloat())
            button.setPosition((UIManager.screenWidth - UIManager.elementWidth) / 2f, pos)

            stage.addActor(button)
            pos -= (UIManager.elementHeight + UIManager.elementSpacing)
        }
    }
}