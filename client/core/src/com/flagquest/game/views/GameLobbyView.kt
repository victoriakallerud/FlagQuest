package com.flagquest.game.views

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.controllers.GameLobbyController
import com.flagquest.game.models.LobbyApiModel
import com.flagquest.game.models.UserApiModel
import com.flagquest.game.navigation.LobbyRedirectionListener
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.states.OnlineGameState
import com.flagquest.game.utils.ButtonClickListener
import com.flagquest.game.utils.DataManager
import com.flagquest.game.utils.UIManager
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

class GameLobbyView(gsm: GameStateManager, isAdmin: Boolean, lobbyId: String, private val stage: Stage/*, listener: LobbyRedirectionListener*/) {
    val controller: GameLobbyController = GameLobbyController(UserApiModel(), LobbyApiModel())

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
        val lobby = controller.onLoadLobby(lobbyId)
        textFieldStyle.font.data.setScale(5f)

        val jsonLobby = JSONObject(lobby)
        val options = jsonLobby.getJSONObject("options")
        totalParticipants = options.getInt("maxNumOfPlayers")

        val playerIdsJson = jsonLobby.getJSONArray("players")
        lobbyInviteCode = jsonLobby.getString("inviteCode").toInt()
        val playerIds = mutableListOf<String>()

        for (i in 0 until playerIdsJson.length()) {
            playerIds.add(playerIdsJson.getString(i))
        }

        for (id in playerIds) {
            val playerName = controller.onLoadPlayer(id)
            names.add(playerName!!)
        }

        currParticipants = playerIds.size
        val joinedText = "$currParticipants/$totalParticipants has joined"

        val lobbyId = DataManager.getData("lobbyId") as String
        val userId = DataManager.getData("userId") as String

        val socket = controller.connectToSocket()
        controller.joinLobby(socket, lobbyId, userId)
        socket.on("updateLobby") { args ->
            val message = args[0] as JSONObject
            println("Lobby updated: ${message.getString("name")} - ${message.getString("state")} - ${message.getString("id")}")
        }
        socket.on("status") { args ->
            val message = if (args[0] is String) JSONObject(args[0] as String) else args[0] as JSONObject
            val status = message.getString("status")
            val statusMessage = if (message.get("message") is JSONArray) {
                val messageArray = message.getJSONArray("message")
                messageArray.joinToString(", ")
            } else {
                message.getString("message")
            }
            if (status == "ERROR") {
                println("Status [ERROR]: $statusMessage")
                // joinLobby(socket)
            } else {
                println("Status [SUCCESS]: $statusMessage")
                // startGame(socket)
            }
        }

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
            startButton.addListener(ButtonClickListener(gsm, lazy { OnlineGameState(gsm) })) //TODO: Link to Online Game once implemented.
            table.add(startButton).width(screenWidth * 0.8f).padTop(100f)
        }

        stage.addActor(table)
        titleFont.data.setScale(1.5f)
    }
}