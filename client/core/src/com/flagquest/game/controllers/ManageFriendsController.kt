package com.flagquest.game.controllers

import com.flagquest.game.models.UserApiModel

class ManageFriendsController(private val model: UserApiModel) {
    private fun onGetFriendId(username: String): String? {
        return model.getUserByName(username)
    }

    fun onSendFriendRequest(friendName: String): String? {
        val friendId = onGetFriendId(friendName)
        return model.putAddFriend(friendId!!)
    }

    fun onGetAllFriendNames(): MutableList<String> {
        return model.getFriendNames()
    }


}