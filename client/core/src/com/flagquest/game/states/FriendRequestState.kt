package com.flagquest.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.flagquest.game.utils.FriendRequestListener
import com.flagquest.game.utils.UIManager.addBackButton
import com.flagquest.game.utils.UIManager.addHeading
class FriendRequestState(gsm: GameStateManager) : State(gsm) {
    private val skin: Skin = Skin(Gdx.files.internal("skins/skin/flat-earth-ui.json"))
    private var screenWidth = Gdx.graphics.width
    private val screenHeight = Gdx.graphics.height
    private val imgBtnHeight = screenHeight / 19
    override val stage = Stage(ScreenViewport())

    init {
        // Heading
        addHeading(stage, "FRIEND\nREQUESTS", 2.8f)
        addBackButton(stage, gsm, backNavType)

        // Friend list
        var friends = arrayOf(
            "Tina Brynislen",
            "Markus Rosenhav",
            "Magnus Hegdahl",
            "Amel, Ruler of the first and second world and beyond",
            "Johan Store",
            "Karel de Grote",
            "Edgar",
            "Rasmus"
        )


        val list = Table().apply {
            defaults().pad(30f)
        }

        val maxNameLength = 18 // Max nr. characters in name before it gets truncated.
        friends.forEach { name -> //Create a row for each name.

            val nameLabel = Label(truncateString(name,maxNameLength), skin).apply {// Only show truncated names.
                setFontScale(3.5f) // Set the font scale to make the text bigger
            }
            // AcceptButton
            val acceptTexture = Texture(Gdx.files.internal("skins/raw/checkbox-pressed.png"))
            val acceptDrawable = TextureRegionDrawable(TextureRegion(acceptTexture)).apply {
                minWidth = imgBtnHeight.toFloat()
                minHeight = imgBtnHeight.toFloat()
            }
            val acceptButton = ImageButton(acceptDrawable).apply {
                addListener(FriendRequestListener(name,"accept")) //Functionality in utils/FriendRequestListener
            }

            // RejectButton
            val rejectTexture = Texture(Gdx.files.internal("skins/raw/button-close.png"))
            val rejectDrawable = TextureRegionDrawable(TextureRegion(rejectTexture)).apply {
                minWidth = imgBtnHeight.toFloat()
                minHeight = imgBtnHeight.toFloat()
            }
            val rejectButton = ImageButton(rejectDrawable).apply {
                addListener(FriendRequestListener(name,"reject"))
            }

            val item = Table().apply {
                add(nameLabel).expandX().fillX().padRight(10f)
                add(acceptButton)
                add(rejectButton)
                row()
            }

            list.add(item).expandX().fillX()
            list.row()
        }

        val scrollPane = ScrollPane(list, skin).apply {
            setScrollingDisabled(true, false)
            setFadeScrollBars(false)

            // Calculate the position to center the scroll pane
            val scrollPaneWidth = (screenWidth / 100 * 80).toFloat() // Assuming 80% of the screen width
            val scrollPaneHeight = (screenHeight/100*48).toFloat() // Assuming a fixed height
            val xPos = (screenWidth - scrollPaneWidth) / 2
            val yPos = (screenHeight - scrollPaneHeight) / 2

            // Set the position of the scroll pane
            setPosition(xPos, yPos)
            setSize(scrollPaneWidth, scrollPaneHeight)
        }

        // Add scroll pane to stage
        stage.addActor(scrollPane)
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
    private fun truncateString(input: String, maxLength: Int): String {
        if (input.length <= maxLength) {
            return input // Return the original string if it's within the maxLength
        } else {
            // Otherwise, truncate the string and append an ellipsis
            return input.substring(0, maxLength - 3) + "..."
        }
    }
}
