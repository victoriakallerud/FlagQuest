package com.flagquest.game.views

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.utils.UIManager
import com.flagquest.game.utils.UIManager.addHeading

class MainMenuView(private val gsm: GameStateManager, private val stage: Stage) {
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val screenHeight = Gdx.graphics.height
    val buttons: Array<TextButton> = arrayOf(
        TextButton("CREATE GAME", skin),
        TextButton("JOIN GAME", skin),
        TextButton("TRAINING MODE", skin),
        TextButton("HIGHSCORE BOARD", skin),
        TextButton("MANAGE FRIENDS", skin)
    )
    private var buttonStartingPos = ((screenHeight / 2) + 150).toFloat()

    init {
        addHeading(stage, "MAIN MENU")
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
            button.setSize(UIManager.buttonWidth.toFloat(), UIManager.buttonHeight.toFloat())
            button.setPosition((UIManager.screenWidth - UIManager.buttonWidth) / 2f, buttonStartingPos)

            stage.addActor(button)
            buttonStartingPos -= (UIManager.buttonHeight + UIManager.buttonSpacing)
        }
    }

    fun render() {
        stage.draw()
    }

    fun update(dt: Float) {
        stage.act(dt)
    }
}
