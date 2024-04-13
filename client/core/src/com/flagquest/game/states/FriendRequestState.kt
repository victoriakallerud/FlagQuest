package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.utils.UIManager.addBackButton
import com.flagquest.game.utils.UIManager.addHeading

class FriendRequestState(gsm: GameStateManager) : State(gsm) {
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val titleFont: BitmapFont = skin.getFont("title")
    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height
    private val buttonHeight = screenHeight / 11
    override val stage = Stage(ScreenViewport())

    // Heading
    private val heading = Label("FRIEND\nREQUESTS", skin)

    // Friend list
    private val friends = arrayOf( // TODO: Implement function to retrieve friend requests
        "Tina Brynislen",
        "Markus Rosenhave",
        "Magnus Hegdahl"
    )
    private val friendsString = friends.joinToString(separator = "\n")
    private val friendsLabel = Label(friendsString, skin)

    // Turn friend list into scrollable friend list
    private val scrollPane = ScrollPane(friendsLabel, skin).apply {
        setScrollingDisabled(true, false)
        setFadeScrollBars(false)
    }

    // Add friend button
    private val addFriendBtn = TextButton("+ ADD FRIEND", skin)

    init {
        // Color of title bar on popup window
        val popupTitleColor = Color(0.011764706f, 0.23921569f, 0.3764706f, 1f)

        // Heading
        addHeading(stage,"MANAGE\nFRIENDS", 2.8f)
        addBackButton(stage,gsm, backNavType)

        // Friend list styling
        friendsLabel.setStyle(Label.LabelStyle(titleFont, friendsLabel.style.fontColor))
        friendsLabel.setFontScale(1.8f)
        scrollPane.setSize((screenWidth/100*80).toFloat(), 700f)
        scrollPane.setPosition((screenWidth - scrollPane.width)/2f, (screenHeight - scrollPane.height)/2f)
        scrollPane.setColor(0.92f, 0.88f, 0.84f, 1f)
        stage.addActor(scrollPane)

        // Add friend button styling
        addFriendBtn.setSize((screenWidth*80/100).toFloat(), buttonHeight.toFloat())
        addFriendBtn.setPosition(screenWidth / 2 - addFriendBtn.width / 2, (screenHeight - scrollPane.height)/2f - buttonHeight - 30)
        stage.addActor(addFriendBtn)

        // Listener that opens popup when add friend button is clicked
        addFriendBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                val popupWindow = Window("WHO DO YOU WANT TO ADD?", skin).apply {
                    isModal = true
                    isMovable = false

                    // Username input field
                    val usernameField = TextField("", skin).apply{ messageText="  Username"}
                    add(usernameField).width(640f).height(150f).padBottom(30f)
                    row()

                    // Add friend button
                    val addBtn = TextButton("+ ADD", skin)

                    // Add listener that (for now) only closes the window
                    addBtn.addListener(object : ClickListener() {
                        override fun clicked(event: InputEvent?, x: Float, y: Float) { // TODO: Add logic to add friend here
                            remove()
                        }
                    })
                    add(addBtn).width(640f).height(100f)

                    pack()

                    // Set size and position
                    setSize(800f, 600f)
                    setPosition(screenWidth / 2 - width / 2, screenHeight / 2 - height / 2)

                    // Edit title bar styling
                    titleLabel.style = Label.LabelStyle(titleFont, titleLabel.style.fontColor)
                    titleLabel.style.font.data.setScale(1f)
                    titleLabel.style.fontColor = popupTitleColor
                    titleTable.padLeft(20f)
                    padTop(70f)
                }

                // Set color of title bar
                val titleBarColor = Color(0.65882355f, 0.83137256f, 0.8627451f, 1f)
                val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
                pixmap.setColor(titleBarColor)
                pixmap.fill()
                val popupTexture = Texture(pixmap)
                popupWindow.titleTable.background = TextureRegionDrawable(TextureRegion(popupTexture))

                stage.addActor(popupWindow)
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