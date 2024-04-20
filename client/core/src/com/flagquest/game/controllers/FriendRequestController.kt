package com.flagquest.game.controllers

import com.flagquest.game.models.UserApiModel

class FriendRequestController(private val model: UserApiModel) {
    fun onGetFriendRequests(): MutableList<Pair<String, String>> {
        return model.getPendingFriendRequests()
    }

    fun onAcceptFriendRequest(friendId: String): String? {
        return model.putAcceptRequest(friendId)
    }

    fun onRejectFriendRequest(friendId: String): String? {
        return model.delRejectRequest(friendId)
    }
}