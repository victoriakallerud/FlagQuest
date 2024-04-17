package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.utils.UIManager.addBackButton
import com.flagquest.game.utils.UIManager.addHeading
import com.flagquest.game.utils.ButtonClickListener
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject

class LobbyInitiationState(gsm: GameStateManager) : State(gsm) {
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val textFieldStyle: TextField.TextFieldStyle = skin.get(TextField.TextFieldStyle::class.java)
    private val titleFont: BitmapFont = skin.getFont("title")
    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height
    private val buttonHeight = screenHeight / 11
    private var pos: Float = ((screenHeight / 2) + 50).toFloat()
    private val code = (10000..99999).random() // TODO: write function to get code
    private val codeIsFree = false
    override val stage = Stage(ScreenViewport())

    private val codeText = Label("Lobby Code: $code", skin)
    private val sizeInput = TextField("", skin).apply{ messageText="  How many players?"}
    private val inviteLinkBtn = TextButton("GET INVITE LINK", skin)
    private val inviteBtn = TextButton("INVITE FRIENDS", skin)
    private val createBtn = TextButton("CREATE LOBBY", skin)
    private var size: Int = 6

    private val btns = arrayOf(inviteLinkBtn, inviteBtn, createBtn)
    private var counter: Int = 1

    init {
        Gdx.input.inputProcessor = stage
        textFieldStyle.font.data.setScale(5f)

        addHeading(stage,"CREATE GAME\nLOBBY", 2.8f)
        addBackButton(stage,gsm, backNavType)

        codeText.setStyle(Label.LabelStyle(titleFont, codeText.style.fontColor))
        codeText.setFontScale(1.5f)
        codeText.pack()
        codeText.setPosition((screenWidth - codeText.prefWidth) / 2, pos + 250f)
        stage.addActor(codeText)

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

                val client = OkHttpClient()
                val mediaType = "application/json".toMediaType()
                val body = ("{" +
                        "\r\n  \"name\": \"testname\"," + //
                        "\r\n  \"admin\": \"873194ce-5f36-4e0c-8c9c-ce2bdedeb3f0\"," + // TODO: Implement function to get user's id
                        "\r\n  \"options\": " +
                        "{\r\n    \"maxNumOfPlayers\": \"$size\"," +
                        "\r\n    \"numberOfQuestions\": 10," +
                        "\r\n    \"showAnswers\": true," +
                        "\r\n    \"gameMode\": \"GuessingFlags\"," +
                        "\r\n    \"level\": \"Europe\"," +
                        "\r\n    \"isPrivate\": false" +
                        "\r\n  }\r\n" +
                        "}").toRequestBody(mediaType)
                val request = Request.Builder()
                    .url("http://flagquest.leotm.de:3000/lobby/")
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("X-API-Key", "{{token}}")
                    .build()
                val response = client.newCall(request).execute()
                val responseBodyString = response.body?.string()
                println("Response Body: $responseBodyString")
                try {
                    responseBodyString?.let {
                        val lobbyId = JSONObject(it).getString("id")
                        println("Lobby ID: $lobbyId")
                        gsm.push(GameLobbyState(gsm, isAdmin = true, lobbyId = lobbyId))
                    }
                } catch (e: JSONException) {
                    println("Failed to parse the response JSON: ${e.message}")
                }
            }
        })



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