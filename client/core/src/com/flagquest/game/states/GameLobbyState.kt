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
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import com.flagquest.game.utils.UIManager.addBackButton
import com.flagquest.game.utils.UIManager.addHeading
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import com.flagquest.game.utils.SocketHandler
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject

class GameLobbyState(gsm: GameStateManager, isAdmin: Boolean, lobbyId: String) : State(gsm) {
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val textFieldStyle: TextField.TextFieldStyle = skin.get(TextField.TextFieldStyle::class.java)
    private val titleFont: BitmapFont = skin.getFont("title")
    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height
    private val buttonHeight = screenHeight / 11
    private var pos: Float = ((screenHeight / 2) + 50).toFloat()
    override var backNavType = "menu"
    override val stage = Stage(ScreenViewport())
    private var currParticipants: Int = 0
    private var totalParticipants: Int = 0
    private val heading = Label("GAME LOBBY", skin)
    private var names: MutableList<String> = mutableListOf()

    init {

        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/lobby/${lobbyId}")
            .addHeader("X-API-Key", "{{token}}")
            .build()

        val response = client.newCall(request).execute()
        val responseBodyString = response.body?.string()

        println("Response Body: $responseBodyString")
        try {
            responseBodyString?.let {
                val lobbyId = JSONObject(it).getString("id")
                println("Lobby ID: $lobbyId")
                val jsonObject = JSONObject(it)
                val optionsObject = jsonObject.getJSONObject("options")

                val playerIdsJson = jsonObject.getJSONArray("players")

                val playerIds = mutableListOf<String>()

                for (i in 0 until playerIdsJson.length()) {
                    playerIds.add(playerIdsJson.getString(i))
                }

                for (id in playerIds) {
                    val playerRequest = Request.Builder()
                        .url("http://flagquest.leotm.de:3000/user/$id")
                        .addHeader("X-API-Key", "{{token}}")
                        .build()
                    val playerResponse = client.newCall(playerRequest).execute()
                    val playerResponseBodyString = playerResponse.body?.string()
                    val playerJsonObject = JSONObject(playerResponseBodyString)
                    val playerName = playerJsonObject.getString("userName")
                    names.add(playerName)
                }
                currParticipants = playerIds.size
                totalParticipants = optionsObject.getInt("maxNumOfPlayers")
                println("Total Participants: $totalParticipants")
            }
        } catch (e: JSONException) {
            println("Failed to parse the response JSON: ${e.message}")
        }

        Gdx.input.inputProcessor = stage
        textFieldStyle.font.data.setScale(5f)

        val codeText = Label("$currParticipants/$totalParticipants has joined", skin)

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