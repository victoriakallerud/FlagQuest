package com.flagquest.game.utils

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener

class FriendRequestListener(private val friendName: String, private val action: String) : ClickListener() {
        override fun clicked(event: InputEvent?, x: Float, y: Float) {
            if (action == "accept"){
                // TODO: Add logic for backend,
                // SEND accept friend to backend
                // Reload friend request state (pop and push)
                println("Accept button clicked for friend: $friendName")
            }
            else if (action == "reject"){
                //TODO: Add logic for the reject button here, using the friendName parameter
                println("Reject button clicked for friend: $friendName")
            }
            else throw Exception("Wrong Input: action.")
        }
}
