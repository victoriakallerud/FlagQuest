package com.flagquest.game.views

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.flagquest.game.controllers.GameLobbyController
import com.flagquest.game.models.LobbyApiModel
import com.flagquest.game.models.UserApiModel
import com.flagquest.game.navigation.LobbyRedirectionListener
import com.flagquest.game.navigation.OnlineGameRedirectionListener
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.utils.UIManager

class GameLobbyView(gsm: GameStateManager, isAdmin: Boolean, lobbyId: String, private val stage: Stage, onlineGameListener: OnlineGameRedirectionListener, lobbyListener: LobbyRedirectionListener) {
    private val controller: GameLobbyController = GameLobbyController(UserApiModel(), LobbyApiModel())

    private val skin: Skin = UIManager.skin
    private val textFieldStyle: TextField.TextFieldStyle = skin.get(TextField.TextFieldStyle::class.java)
    private val titleFont: BitmapFont = UIManager.titleFont
    private var screenWidth = UIManager.screenWidth
    private val screenHeight = UIManager.screenHeight
    private val buttonHeight = UIManager.elementHeight
    private var pos: Float = ((screenHeight / 2) + 50).toFloat()
    private var backNavType = "menu"
    private var currParticipants: Int = 0
    private var totalParticipants: Int = 0
    private val heading = Label("GAME LOBBY", skin)
    private var names: MutableList<String> = mutableListOf()
    private var lobbyInviteCode: Int = -1

    init {
        controller.onlineGameRedirectionListener = onlineGameListener
        controller.lobbyRedirectionListener = lobbyListener
        controller.attachQuizListener()
        controller.attachUpdateLobbyListener()

        val lobby = controller.onLoadLobby(lobbyId)
        textFieldStyle.font.data.setScale(5f)

        totalParticipants = controller.getMaxPlayers(lobby!!)
        lobbyInviteCode = controller.getLobbyCode(lobby!!)
        val playerIdsJson = controller.getPlayerIdsJson(lobby!!)
        val playerIds = mutableListOf<String>()

        for (i in 0 until playerIdsJson!!.length()) {
            playerIds.add(playerIdsJson.getString(i))
        }

        for (id in playerIds) {
            val playerName = controller.onLoadPlayer(id)
            names.add(playerName!!)
        }

        currParticipants = playerIds.size
        val joinedText = "$currParticipants/$totalParticipants has joined"


        UIManager.addHeading(stage, "GAME LOBBY", 2.8f)
        UIManager.addBackButton(stage, gsm, backNavType)

        // Display lobby number
        val lobbyCodeText = "Lobby Code: $lobbyInviteCode"
        val lobbyTextY = pos + 400f
        if(lobbyInviteCode == -1)
            UIManager.addHeading(
                stage,
                "ERROR PARSING LOBBY",
                1.5f,
                lobbyTextY
            ) // If lobby code not parsed
        else
            UIManager.addHeading(stage, lobbyCodeText, 1.5f, lobbyTextY)

        // Display number of people joined
        val joinedTextY = pos + 250f
        UIManager.addHeading(stage, joinedText, 1.5f, joinedTextY)

        // Display players that have joined
        val table = Table()
        table.setFillParent(true)

        println("Names: $names")

        for (name in names) {
            val nameLabel = Label(name, skin)
            nameLabel.setStyle(Label.LabelStyle(titleFont, heading.style.fontColor))
            nameLabel.setFontScale(1f)
            table.add(nameLabel).pad(10f)
            table.row()
        }

        if (isAdmin) {
            val startButton = TextButton("START NOW", skin)
            startButton.height = buttonHeight.toFloat()
            startButton.setPosition(screenWidth / 2 - startButton.width / 2, 300f)
            startButton.addListener(object: ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    controller.onGameStartButtonClicked(lobbyId)
                }

            }) //TODO: Link to Online Game once implemented. // TODO: Add send "startGame" on WebSocket
            table.add(startButton).width(screenWidth * 0.8f).padTop(100f)
        }

        stage.addActor(table)
        titleFont.data.setScale(1.5f)
    }
}