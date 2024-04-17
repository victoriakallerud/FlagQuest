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
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.utils.ButtonClickListener
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import com.flagquest.game.utils.UIManager.addBackButton
import com.flagquest.game.utils.UIManager.addHeading

class GameLobbyState(gsm: GameStateManager, isAdmin: Boolean, lobbyId: String) : State(gsm) {
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val textFieldStyle: TextField.TextFieldStyle = skin.get(TextField.TextFieldStyle::class.java)
    private val titleFont: BitmapFont = skin.getFont("title")
    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height
    private val buttonHeight = screenHeight / 11
    private var pos: Float = ((screenHeight / 2) + 50).toFloat()
    private val stage = Stage(ScreenViewport())
    private var currParticipants: Int = 0
    private var totalParticipants: Int = 0
    private val heading = Label("GAME LOBBY", skin)
    private var names: MutableList<String> = mutableListOf()

    private val lobbyCodeText = "Lobby Code: $lobbyCode" //Displays code with leading zeroes
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
        textFieldStyle.font.data.setScale(5f)

        addHeading(stage, "GAME LOBBY", 2.8f)
        addBackButton(stage,gsm, backNavType)

        // Display lobby number
        val lobbyTextY = pos + 400f
        addHeading(stage,lobbyCodeText,1.5f, lobbyTextY)

        // Display number of people joined
        val joinedTextY = pos + 250f
        addHeading(stage,joinedText, 1.5f, joinedTextY)

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
}