package com.flagquest.game.controllers

import com.flagquest.game.models.LobbyApiModel
import com.flagquest.game.models.UserApiModel
import com.flagquest.game.utils.SocketHandler
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject

class GameLobbyController(private val userModel: UserApiModel, private val lobbyModel: LobbyApiModel) {
    private lateinit var mSocket: Socket

    fun onLoadLobby(lobbyId: String): String? {
        return lobbyModel.getLobby(lobbyId)
    }

    fun onLoadPlayer(playerId: String): String? {
        val player = userModel.getUserById(playerId)
        val jsonPlayer = JSONObject(player)
        return jsonPlayer.getString("userName")
    }

    fun connectToSocket(): Socket {
        SocketHandler.setSocket()
        SocketHandler.establishConnection()

        mSocket = SocketHandler.getSocket()

        mSocket.on(Socket.EVENT_CONNECT) {
            println("Connected to server")
        }
        mSocket.on("updateLobby") { args ->
            val message = args[0] as JSONObject
            println("Lobby updated: ${message.getString("name")} - ${message.getString("state")} - ${message.getString("id")}")
        }
        mSocket.on("status") { args ->
            val message = if (args[0] is String) JSONObject(args[0] as String) else args[0] as JSONObject
            val status = message.getString("status")
            val statusMessage = if (message.get("message") is JSONArray) {
                val messageArray = message.getJSONArray("message")
                messageArray.joinToString(", ")
            } else {
                message.getString("message")
            }
            if (status == "ERROR") {
                println("Status [ERROR]: $statusMessage")
                // joinLobby(socket)
            } else {
                println("Status [SUCCESS]: $statusMessage")
                // startGame(socket)
            }
        }

        return mSocket
    }

    fun joinLobby(socket: Socket, userId: String, lobbyId: String) {
        val joinBody = JSONObject()
        joinBody.put("userId", userId)
        joinBody.put("lobbyId", lobbyId)
        println("Joining lobby with object: ${joinBody.toString()}")
        socket.emit("joinLobby", joinBody)
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