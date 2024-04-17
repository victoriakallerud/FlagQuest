package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ScreenViewport
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import com.flagquest.game.utils.UIManager.addBackButton
import com.flagquest.game.utils.UIManager.addHeading

class JoinGameState(gsm: GameStateManager) : State(gsm) {
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val titleFont: BitmapFont = skin.getFont("title")
    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height
    private val buttonHeight = screenHeight / 11
    private var pos: Float = ((screenHeight / 2) + 50).toFloat()
    override val stage = Stage(ScreenViewport())

    private val codeInput = TextField("", skin).apply{ messageText="  GAME CODE"}
    private val codeBtn = TextButton("JOIN WITH CODE", skin)
    private val randomBtn = TextButton("JOIN RANDOM GAME", skin)

    private val btns = arrayOf(
        codeBtn to lazy { GameLobbyState(gsm, isAdmin = false, "14d29155-82ef-4d11-9c36-7214d1a8e4b7") }, //TODO: Add backend logic
        randomBtn to lazy { GameLobbyState(gsm, isAdmin = false, "14d29155-82ef-4d11-9c36-7214d1a8e4b7") } ) //TODO: Add backend logic
    private var counter: Int = 1

    init {
        addHeading(stage,"JOIN GAME\n LOBBY", 2.8f)
        addBackButton(stage,gsm, backNavType)

        codeInput.width = (screenWidth*80/100).toFloat()
        codeInput.height = buttonHeight.toFloat()
        codeInput.setPosition(screenWidth / 2 - codeInput.width / 2, pos)
        stage.addActor(codeInput)

        titleFont.data.setScale(1.5f)
        for (btn in btns) {
            btn.first.width = (screenWidth*80/100).toFloat()
            btn.first.height = buttonHeight.toFloat()
            btn.first.setPosition(screenWidth / 2 - btn.first.width / 2, pos - (buttonHeight + 30) * counter)
            //btn.first.addListener(ButtonClickListener(gsm, btn.second))
            stage.addActor(btn.first)
            counter++
        }

        codeBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                var code: String = ""
                if (codeInput.text != "") {
                    code = codeInput.text
                    println("Code: $code")
                    val client = OkHttpClient()
                    val mediaType = "text/plain".toMediaType()
                    val body = "".toRequestBody(mediaType)
                    val request = Request.Builder()
                        .url("http://flagquest.leotm.de:3000/lobby/invite/$code/87a56b6f-31e5-42d1-9f55-a3ee8c2fe5c0") // lobbyId/userId TODO: Add functionality to use the device's user's ID
                        .put(body)
                        .addHeader("X-API-Key", "{{token}}")
                        .build()
                    val response = client.newCall(request).execute()
                    val responseBodyString = response.body?.string()
                    println("Response Body: $responseBodyString")
                    try {
                        responseBodyString?.let {
                            val lobbyId = JSONObject(it).getString("id")
                            println("Lobby ID: $lobbyId")
                            gsm.push(GameLobbyState(gsm, isAdmin = false, lobbyId = lobbyId))
                        }
                    } catch (e: JSONException) {
                        println("Failed to parse the response JSON: ${e.message}")
                    }
                }

            }
        })

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