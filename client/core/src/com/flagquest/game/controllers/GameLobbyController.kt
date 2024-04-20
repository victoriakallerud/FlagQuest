package com.flagquest.game.controllers

import com.badlogic.gdx.Gdx
import com.flagquest.game.models.LobbyApiModel
import com.flagquest.game.models.UserApiModel
import com.flagquest.game.navigation.LobbyRedirectionListener
import com.flagquest.game.navigation.OnlineGameRedirectionListener
import com.flagquest.game.utils.DataManager
import com.flagquest.game.utils.SocketHandler
import org.json.JSONArray
import org.json.JSONObject

class GameLobbyController(private val userModel: UserApiModel, private val lobbyModel: LobbyApiModel) {
    var onlineGameRedirectionListener: OnlineGameRedirectionListener? = null
    var lobbyRedirectionListener: LobbyRedirectionListener? = null

    fun attachQuizListener() {
        SocketHandler.getSocket().on("quiz") { args ->
            val message = args[0] as JSONObject
            Gdx.app.log("GameApiModel", "quiz: $message")
            val questions: JSONArray = message.get("questions") as JSONArray
            DataManager.setData("questions", questions)
            DataManager.setData("currentQuestionIndex", 0)
            Gdx.app.log("GameApiModel", "questions are now set to: $questions")
            // Run with postRunnable:
            Gdx.app.postRunnable {
                onlineGameRedirectionListener?.redirectToOnlineGameState()
            }
            // redirectionListener?.redirectToOnlineGameState()
        }
    }

    fun attachUpdateLobbyListener() {
        SocketHandler.getSocket().on("updateLobby") { args ->
            val message = args[0] as JSONObject
            Gdx.app.log("GameApiModel", "updateLobby: $message")
            val players = message.getJSONArray("players")
            val lobbyId = message.getString("id")
            Gdx.app.log("GameApiModel", "players are now set to: $players")
            Gdx.app.postRunnable {
                detachQuizListener()
                detachUpdateLobbyListener()
                lobbyRedirectionListener?.redirectToLobbyState(lobbyId)
            }
        }
    }

    private fun detachUpdateLobbyListener() {
        SocketHandler.getSocket().off("updateLobby")
    }

    private fun detachQuizListener() {
        SocketHandler.getSocket().off("quiz")
    }

    fun onGameStartButtonClicked(lobbyId: String) {
        SocketHandler.emit("startGame", JSONObject().put("lobbyId", lobbyId).put("userId", DataManager.getData("userId")))
        // Wait for 2 seconds
//        Thread.sleep(2000)
//        redirectionListener?.redirectToOnlineGameState()
    }

    fun onLoadLobby(lobbyId: String): String? {
        return lobbyModel.getLobby(lobbyId)
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