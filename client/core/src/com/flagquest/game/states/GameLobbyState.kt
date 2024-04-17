package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.utils.ButtonClickListener
import com.flagquest.game.utils.SocketHandler
import com.flagquest.game.utils.UIManager.addBackButton
import com.flagquest.game.utils.UIManager.addHeading
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject

class GameLobbyState(gsm: GameStateManager, isAdmin: Boolean) : State(gsm) {
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val textFieldStyle: TextField.TextFieldStyle = skin.get(TextField.TextFieldStyle::class.java)
    private val titleFont: BitmapFont = skin.getFont("title")
    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height
    private val buttonHeight = screenHeight / 11
    private var pos: Float = ((screenHeight / 2) + 50).toFloat()
    override val stage = Stage(ScreenViewport())
    override var backNavType = "menu"

    private val currParticipants: Int = 4 // TODO: Implement way of getting current participants number
    private val totalParticipants: Int = 6 // TODO: Implement way of getting total participants number

    private val heading = Label("GAME LOBBY", skin)
    private val codeText = Label("$currParticipants/$totalParticipants has joined", skin)
    private val names = arrayOf("Amel De Kok", "Felix Kuhn", "Leo Laisé", "Victoria Kallerud") // TODO: Implement way of getting participants

    init {
        val lobbyId = "51bdfbe3-88b7-4ed5-8c71-079adc346026" // TODO: Implement way of getting code
        val userId = "97586711-7473-4e21-867a-41dd65faaec1" // TODO: Implement way of getting user id

        SocketHandler.setSocket()
        SocketHandler.establishConnection()

        val mSocket = SocketHandler.getSocket()

        mSocket.on(Socket.EVENT_CONNECT) {
            println("Connected to server")
        }

        joinLobby(mSocket, userId, lobbyId)

        mSocket.on("updateLobby") { args ->
            val message = args[0] as JSONObject
            println("Lobby updated: ${message.getString("name")} - ${message.getString("state")} - ${message.getString("id")}")
        }

        mSocket.on("status") { args ->
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

        textFieldStyle.font.data.setScale(5f)

        addHeading(stage, "GAME LOBBY", 2.8f)
        addBackButton(stage,gsm, backNavType)

        codeText.setStyle(Label.LabelStyle(titleFont, codeText.style.fontColor))
        codeText.setFontScale(1.5f)
        codeText.pack()
        codeText.setPosition((screenWidth - codeText.prefWidth) / 2, pos + 250f)
        stage.addActor(codeText)

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

    override fun handleInput() {
        screenWidth = screenWidth // Just placeholder code to make the code compile
    }
    override fun update(dt: Float) {
        handleInput()
        stage.act(dt)
    }
    override fun render() {
        Gdx.gl.glClearColor(0.92f, 0.88f, 0.84f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.draw()
    }

    private fun joinLobby(socket: Socket, userId: String, lobbyId: String) {
        val joinBody = JSONObject()
        joinBody.put("userId", userId)
        joinBody.put("lobbyId", lobbyId)
        println("Joining lobby with object: ${joinBody.toString()}")
        socket.emit("joinLobby", joinBody)
    }

}