package com.flagquest.game.views

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.flagquest.game.controllers.JoinGameController
import com.flagquest.game.navigation.GameRedirectionListener
import com.flagquest.game.navigation.LobbyRedirectionListener
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.utils.UIManager
import com.flagquest.game.utils.UIManager.addBackButton
import com.flagquest.game.utils.UIManager.addHeading

class JoinGameView(gsm: GameStateManager, stage: Stage, listener: LobbyRedirectionListener, private val controller: JoinGameController) {
    private val skin: Skin = UIManager.skin
    private val titleFont: BitmapFont = UIManager.titleFont
    private var screenWidth = UIManager.screenWidth
    private val screenHeight = UIManager.screenHeight
    private val elementHeight = UIManager.elementHeight
    private var pos: Float = ((screenHeight / 2) + 50).toFloat()
    private val codeInput = TextField("", skin).apply{ messageText="GAME CODE"}
    private val codeBtn = TextButton("JOIN WITH CODE", skin)
    private val randomBtn = TextButton("JOIN RANDOM GAME", skin)
    private val btns = arrayOf( codeBtn, randomBtn )
    private var counter: Int = 1
    val backNavType = "menu"

    init {
        controller.redirectionListener = listener

        addHeading(stage, "JOIN GAME\n LOBBY", 2.8f)
        addBackButton(stage, gsm, backNavType)

        // Add padding to textfield
        skin.get(TextField.TextFieldStyle::class.java).apply {
            background.leftWidth = 50f // Set left padding
        }

        codeInput.width = (screenWidth * 80 / 100).toFloat()
        codeInput.height = elementHeight.toFloat()
        codeInput.setPosition(screenWidth / 2 - codeInput.width / 2, pos)
        stage.addActor(codeInput)

        titleFont.data.setScale(1.5f)
        for (btn in btns) {
            btn.width = (screenWidth * 80 / 100).toFloat()
            btn.height = elementHeight.toFloat()
            btn.setPosition(screenWidth / 2 - btn.width / 2,pos - (elementHeight + 30) * counter)
            stage.addActor(btn)
            counter++
        }

        codeBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                var code: String
                if (codeInput.text != "") {
                    code = codeInput.text
                    val res = controller.onCodeButtonClicked(code)
                    if (!res) {
                        Gdx.app.error("JoinGameView","Failed to join lobby with code")
                        UIManager.removeErrors(stage)
                        UIManager.addError(stage, "Failed to join lobby with code")
                    }
                }
            }
        })

        randomBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                val res: Boolean = controller.onRandomButtonClicked()
                if (!res) {
                    Gdx.app.error("JoinGameView","Failed to join random lobby")
                    UIManager.removeErrors(stage)
                    UIManager.addError(stage, "Failed to join random lobby")
                }
            }
        })
    }
}