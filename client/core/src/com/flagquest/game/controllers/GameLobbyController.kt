package com.flagquest.game.controllers

import com.flagquest.game.models.LobbyApiModel
import com.flagquest.game.models.UserApiModel
import com.flagquest.game.navigation.LobbyRedirectionListener
import com.flagquest.game.utils.SocketHandler
import io.socket.client.Socket

class GameLobbyController(private val userModel: UserApiModel, private val lobbyModel: LobbyApiModel) {
    //var redirectionListener: LobbyRedirectionListener? = null
    private val mSocket = SocketHandler.getSocket()

    fun onLoadLobby(lobbyId: String): String? {
        val lobby = lobbyModel.getLobby(lobbyId)
        return lobby
    }

    fun onLoadPlayer(playerId: String): String? {
        val player = userModel.getUserById(playerId)
        return player
    }

    fun connectToSocket(): Socket {
        SocketHandler.setSocket()
        SocketHandler.establishConnection()

        mSocket.on(Socket.EVENT_CONNECT) {
            println("Connected to server")
        }

        return mSocket
    }

    fun joinLobby(mSocket: Socket, userId: String, lobbyId: String) {
        joinLobby(mSocket, userId, lobbyId)
    }
}