package com.flagquest.game.controllers

import com.badlogic.gdx.Gdx
import com.flagquest.game.models.LobbyApiModel
import com.flagquest.game.models.UserApiModel
import com.flagquest.game.navigation.GameRedirectionListener
import com.flagquest.game.navigation.OnlineGameRedirectionListener
import com.flagquest.game.utils.DataManager
import com.flagquest.game.utils.SocketHandler
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject

class GameLobbyController(private val userModel: UserApiModel, private val lobbyModel: LobbyApiModel) {
    var redirectionListener: OnlineGameRedirectionListener? = null

    fun onGameStartButtonClicked(lobbyId: String) {
        SocketHandler.emit("startGame", JSONObject().put("lobbyId", lobbyId).put("userId", DataManager.getData("userId")))
        // Wait for 2 seconds
        Thread.sleep(2000)
        redirectionListener?.redirectToOnlineGameState()
    }

    fun onLoadLobby(lobbyId: String): String? {
        val lobby = lobbyModel.getLobby(lobbyId)
        return lobby
    }

    fun onLoadPlayer(playerId: String): String? {
        val player = userModel.getUserById(playerId)
        val jsonPlayer = JSONObject(player)
        return jsonPlayer.getString("userName")
    }


    fun getMaxPlayers(lobby: String): Int {
        val options = JSONObject(lobby).getJSONObject("options")
        return options.getInt("maxNumOfPlayers")
    }

    fun getLobbyCode(lobby: String): Int {
        return JSONObject(lobby).getString("inviteCode").toInt()
    }

    fun getPlayerIdsJson(lobby: String): JSONArray? {
        return JSONObject(lobby).getJSONArray("players")
    }
}