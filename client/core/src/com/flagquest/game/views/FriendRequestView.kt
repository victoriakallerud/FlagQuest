package com.flagquest.game.views

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.flagquest.game.controllers.FriendRequestController
import com.flagquest.game.models.UserApiModel
import com.flagquest.game.states.GameStateManager
import com.flagquest.game.utils.UIManager
import com.flagquest.game.utils.UIManager.addScrollPane
import com.flagquest.game.utils.UIManager.truncateString

class FriendRequestView(gsm: GameStateManager, stage: Stage) {
    private val controller: FriendRequestController = FriendRequestController(UserApiModel())
    private val skin: Skin = UIManager.skin
    private val imgBtnHeight = UIManager.elementHeight
    private val backNavType = "menu"
    private val maxStringLength = 15 // Cut-off point for string size.
    private val table = Table().apply {
        defaults().pad(30f)
    }

    init {
        // Heading
        UIManager.addHeading(stage, "FRIEND\nREQUESTS", 2.8f)
        UIManager.addBackButton(stage, gsm, backNavType)

        val friends = controller.onGetFriendRequests()

        friends.forEach { name -> //Create a row for each name.

            val nameLabel = Label(truncateString(name.first, maxStringLength), skin, "title").apply {// Only show truncated names.
                setFontScale(1.1f) // Smaller font to display more of name
            }

            // AcceptButton
            val acceptButton = createButtons(true, name)

            // RejectButton
            val rejectButton = createButtons(false, name)

            val item = Table().apply {
                add(nameLabel).expandX().fillX().padRight(10f)
                add(acceptButton).padRight(10f) //Added padding
                add(rejectButton)
                row()
            }

            table.add(item).expandX().fillX()
            table.row()
        }

        val scrollPane = addScrollPane(table)

        // Add scroll pane to stage
        stage.addActor(scrollPane)
    }


    private fun updateList(stage: Stage) {
        table.clearChildren()
        val newFriends = controller.onGetFriendRequests()
        newFriends.forEach { name -> //Create a row for each name.
            val nameLabel = Label(truncateString(name.first, maxStringLength), skin, "title").apply {// Only show truncated names.
                setFontScale(1.1f) // Smaller font to display more of name
            }
            // AcceptButton
            val acceptButton = createButtons(true, name)

            // RejectButton
            val rejectButton = createButtons(false, name)

            // Add items to row
            val item = Table().apply {
                add(nameLabel).expandX().fillX().padRight(10f)
                add(acceptButton).padRight(10f) //Added padding
                add(rejectButton)
                row()
            }

            table.add(item).expandX().fillX()
            table.row()
        }

        val newScrollPane = addScrollPane(table)

        // Add scroll pane to stage
        stage.addActor(newScrollPane)
    }

    private fun createButtons(isAccept: Boolean, name: Pair<String, String>): ImageButton {
        if (isAccept) {
            val acceptTexture = Texture(Gdx.files.internal("skins/raw/checkbox-pressed.png"))
            val acceptDrawable = TextureRegionDrawable(TextureRegion(acceptTexture)).apply {
                minWidth = imgBtnHeight.toFloat()
                minHeight = imgBtnHeight.toFloat()
            }
            val acceptButton = ImageButton(acceptDrawable).apply {
                addListener(object : ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        controller.onAcceptFriendRequest(name.second)
                        updateList(stage)
                    }
                })
            }
            return acceptButton
        } else {
            val rejectTexture = Texture(Gdx.files.internal("skins/raw/button-close.png"))
            val rejectDrawable = TextureRegionDrawable(TextureRegion(rejectTexture)).apply {
                minWidth = imgBtnHeight.toFloat()
                minHeight = imgBtnHeight.toFloat()
            }
            val rejectButton = ImageButton(rejectDrawable).apply {
                addListener(object : ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        controller.onRejectFriendRequest(name.second)
                        updateList(stage)
                    }
                })
            }
            return rejectButton
        }
    }
}