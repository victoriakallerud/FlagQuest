package com.flagquest.game.views

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.flagquest.game.controllers.LobbyInitiationController
import com.flagquest.game.models.LobbyApiModel
import com.flagquest.game.states.GameLobbyState
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.utils.ButtonClickListener
import com.flagquest.game.utils.UIManager
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class LobbyInitiationView(gsm: GameStateManager, private val stage: Stage) {
    val controller: LobbyInitiationController = LobbyInitiationController(LobbyApiModel(), this)

    private val skin: Skin = UIManager.skin
    private val textFieldStyle: TextField.TextFieldStyle = skin.get(TextField.TextFieldStyle::class.java)
    private val titleFont: BitmapFont = UIManager.titleFont

    private var screenWidth = UIManager.screenWidth
    private val screenHeight = UIManager.screenHeight
    private val buttonHeight = UIManager.buttonHeight
    private var pos: Float = ((screenHeight / 2) + 50).toFloat()

    private val sizeInput = TextField("", skin).apply{ messageText="  How many players?"}
    private val inviteLinkBtn = TextButton("GET INVITE LINK", skin)
    private val inviteBtn = TextButton("INVITE FRIENDS", skin)
    private val createBtn = TextButton("CREATE LOBBY", skin)
    private var size: Int = 6
    val btns = arrayOf( inviteLinkBtn, inviteBtn, createBtn )
    private var counter: Int = 1

    val backNavType = "menu"

    init {
        textFieldStyle.font.data.setScale(5f)

        UIManager.addHeading(stage, "CREATE GAME\nLOBBY", 2.8f)
        UIManager.addBackButton(stage, gsm, backNavType)

        sizeInput.width = (screenWidth*80/100).toFloat()
        sizeInput.height = buttonHeight.toFloat()
        sizeInput.setPosition(screenWidth / 2 - sizeInput.width / 2, pos)
        stage.addActor(sizeInput)

        createBtn.setColor(0.349f, 0.631f, 0.541f, 1f)

        for (btn in btns) {
            btn.width = (screenWidth*80/100).toFloat()
            btn.height = buttonHeight.toFloat()
            btn.setPosition(screenWidth / 2 - btn.width / 2, pos - (buttonHeight + 30) * counter)
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

                val lobby = controller.onCreateGameClicked(size)

                println("Lobby created: $lobby")
                gsm.push(GameLobbyState(gsm, isAdmin = true))
            }
        })
        titleFont.data.setScale(1.5f)
    }

    fun render() {
        stage.draw()
    }

    fun update(dt: Float) {
        stage.act(dt)
    }
}