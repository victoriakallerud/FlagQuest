package com.flagquest.game.views

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.flagquest.game.controllers.LobbyInitiationController
import com.flagquest.game.models.LobbyApiModel
import com.flagquest.game.navigation.LobbyRedirectionListener
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.utils.ButtonClickListener
import com.flagquest.game.utils.UIManager

class LobbyInitiationView(gsm: GameStateManager, private val stage: Stage, listener: LobbyRedirectionListener) {
    val controller: LobbyInitiationController = LobbyInitiationController(LobbyApiModel())

    private val skin: Skin = UIManager.skin
    private val textFieldStyle: TextField.TextFieldStyle = skin.get(TextField.TextFieldStyle::class.java)
    private val titleFont: BitmapFont = UIManager.titleFont
    private var screenWidth = UIManager.screenWidth
    private var pos: Float = ((UIManager.screenHeight / 2) + 50).toFloat()
    private val sizeInput = TextField("", skin).apply{ messageText="  How many players?"}
    private val inviteLinkBtn = TextButton("GET INVITE LINK", skin)
    private val inviteBtn = TextButton("INVITE FRIENDS", skin)
    private val createBtn = TextButton("CREATE LOBBY", skin)
    private var size: Int = 6
    private val btns = arrayOf( inviteLinkBtn, inviteBtn, createBtn )
    private var counter: Int = 1
    val backNavType = "menu"



    init {
        controller.redirectionListener = listener

        textFieldStyle.font.data.setScale(5f)

        UIManager.addHeading(stage, "CREATE GAME\nLOBBY", 2.8f)
        UIManager.addBackButton(stage, gsm, backNavType)

        sizeInput.width = (screenWidth*80/100).toFloat()
        sizeInput.height = UIManager.elementHeight.toFloat()
        sizeInput.setPosition(screenWidth / 2 - sizeInput.width / 2, pos)
        stage.addActor(sizeInput)

        createBtn.setColor(0.349f, 0.631f, 0.541f, 1f)

        for (btn in btns) {
            btn.width = (screenWidth*80/100).toFloat()
            btn.height = UIManager.elementHeight.toFloat()
            btn.setPosition(screenWidth / 2 - btn.width / 2, pos - (UIManager.elementHeight + UIManager.elementSpacing) * counter)
            stage.addActor(btn)
            counter++
        }

        inviteLinkBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                // TODO: Implement function to copy an invite link to the user's phone and show "Invite link copied" at the bottom
            }
        })
        inviteBtn.addListener(ButtonClickListener(gsm,null)) // TODO: Implement invite friends screen and link it here
        createBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (sizeInput.text != "") {
                    size = sizeInput.text.toInt()
                }
                if(!controller.onCreateGameClicked(size)) {
                    Gdx.app.log("LobbyInitiationView", "Failed to create lobby")
                    showError("Failed to create lobby")
                }
            }
        })
        titleFont.data.setScale(1.5f)
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