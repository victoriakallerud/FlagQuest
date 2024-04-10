package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels.TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.viewport.ScreenViewport

class ManageFriendsState(gsm: GameStateManager) : State(gsm) {
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private val textFieldStyle: TextField.TextFieldStyle = skin.get(TextField.TextFieldStyle::class.java)
    private val titleFont: BitmapFont = skin.getFont("title")
    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height
    private val buttonHeight = screenHeight / 11
    private var pos: Float = ((screenHeight / 2) + 50).toFloat()
    private val stage = Stage(ScreenViewport())
    /*
    private val dialogTitleFont = BitmapFont(skin.getFont("title").data, skin.getFont("title").regions, false).apply {
        data.setScale(5f) // Apply your scaling
    }

     */

    private val heading = Label("MANAGE\nFRIENDS", skin)
    private val friends = arrayOf( // TODO: Implement function to retrieve friends
        "Amel De Kok",
        "Felix Kuhn",
        "Håvar Hagelund",
        "Leo Laisé",
        "Nina Tran",
        "Victoria Kallerud",
        "Hanna Lunne",
        "Sindre Havn",
        "Magnus Lien",
        "Urban Mikic",
        "Johan Storesund",
        "Inge Grelland",
        "Elinda Engvik",
        "Miriam Matson",
        "Markus Veblungsnes",
        "Otto Fearnley",
        "Ola Haldor Hådi",
        "Tina Brynislen",
        "Markus Rosenhave",
        "Magnus Hegdahl"
    )
    private val friendsString = friends.joinToString(separator = "\n")
    private val friendsLabel = Label(friendsString, skin)
    private val scrollPane = ScrollPane(friendsLabel, skin).apply {
        setScrollingDisabled(true, false)
        setFadeScrollBars(false)
    }
    private val addFriendBtn = TextButton("+ ADD FRIEND", skin)

    init {
        Gdx.input.inputProcessor = stage
        textFieldStyle.font.data.setScale(5f)
        val popupTitleColor = Color(0.011764706f, 0.23921569f, 0.3764706f, 1f)

        heading.setStyle(Label.LabelStyle(titleFont, heading.style.fontColor))
        heading.setFontScale(2.8f)
        heading.pack()
        heading.setPosition((screenWidth - heading.prefWidth) / 2, screenHeight - 500f)
        stage.addActor(heading)

        friendsLabel.setStyle(Label.LabelStyle(titleFont, friendsLabel.style.fontColor))
        friendsLabel.setFontScale(1.3f)
        scrollPane.setSize((screenWidth/100*80).toFloat(), 700f)
        scrollPane.setPosition((screenWidth - scrollPane.width)/2f, (screenHeight - scrollPane.height)/2f)
        scrollPane.setColor(0.92f, 0.88f, 0.84f, 1f)
        stage.addActor(scrollPane)

        addFriendBtn.setSize((screenWidth*80/100).toFloat(), buttonHeight.toFloat())
        addFriendBtn.setPosition(screenWidth / 2 - addFriendBtn.width / 2, (screenHeight - scrollPane.height)/2f - buttonHeight - 30)
        stage.addActor(addFriendBtn)


        addFriendBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                val popupWindow = Window("WHO DO YOU WANT TO ADD?", skin).apply {
                    isModal = true

                    val usernameField = TextField("", skin).apply{ messageText="  Username"}
                    add(usernameField).width(640f).height(150f).padBottom(30f)
                    row()

                    val addBtn = TextButton("+ ADD FRIEND", skin)

                    addBtn.addListener(object : ClickListener() {
                        override fun clicked(event: InputEvent?, x: Float, y: Float) {
                            remove()
                        }
                    })
                    add(addBtn).width(640f).height(100f)

                    pack()
                    setSize(800f, 600f)
                    setPosition(screenWidth / 2 - width / 2, screenHeight / 2 - height / 2)

                    isMovable = false

                    titleLabel.style = Label.LabelStyle(titleFont, titleLabel.style.fontColor)
                    titleLabel.style.font.data.setScale(1f)
                    titleLabel.style.fontColor = popupTitleColor

                    titleTable.pad(20f, 20f, 20f, 20f)
                    padTop(70f)
                }

                val titleBarColor = Color(0.65882355f, 0.83137256f, 0.8627451f, 1f) // RGBA for blue
                val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
                pixmap.setColor(titleBarColor)
                pixmap.fill()
                val popupTexture = Texture(pixmap)
                pixmap.dispose()
                val popupDrawable = TextureRegionDrawable(TextureRegion(popupTexture))
                popupWindow.titleTable.background = popupDrawable

                stage.addActor(popupWindow)
            }
        })
        /*

        val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888).apply {
            setColor(0.6964705f, 0.9474509f, 0.9984313f, 1f) // Set your color
            fill()
        }

        val texture = Texture(pixmap) // Create a texture from pixmap
        pixmap.dispose() // Dispose pixmap as it's no longer needed

        val backgroundDrawable = TextureRegionDrawable(TextureRegion(texture))

        val dialogStyle = Window.WindowStyle().apply {
            titleFont = dialogTitleFont
            titleFont.data.setScale(3f)
            titleFontColor = Color.PINK // TODO: Change color to match the theme
            background = backgroundDrawable
        }

        addFriendBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                val dialog = Dialog("WHO DO YOU WANT TO ADD?", skin).apply {
                    style = dialogStyle
                    text("This is the content of the popup.")
                    button("OK", true) // The second parameter is the result object
                }
                dialog.show(stage)
                dialog.setSize(800f, 600f)
                dialog.setPosition((screenWidth - dialog.width) / 2f, (screenHeight - dialog.height) / 2f)

            }
        })

         */

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