package com.flagquest.game.utils

import com.badlogic.gdx.Gdx
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

object SocketHandler {

    private lateinit var mSocket: Socket

    @Synchronized
    fun setSocket() {
        mSocket = IO.socket("http://flagquest.leotm.de:3000")
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun removeListener(event: String, listener: (Any) -> Unit) {
        mSocket.off(event, listener)
    }

    @Synchronized
    fun addAllListeners() {
        mSocket.on("connection") {
            Gdx.app.log("SocketHandler", "Connected to WebSocket")
        }
        mSocket.on("status") { args ->
            val message = if (args[0] is String) JSONObject(args[0] as String) else args[0] as JSONObject
            Gdx.app.log("SocketHandler", "status: $message")
        }
        mSocket.on("updateLobby") { args ->
            val message = args[0] as JSONObject
            Gdx.app.log("SocketHandler", "updateLobby: $message")
        }
        mSocket.on("quiz") { args ->
            val message = args[0] as JSONObject
            Gdx.app.log("SocketHandler", "quiz: $message")
        }
        mSocket.on("answerCount") { args ->
            val message = args[0] as JSONObject
            Gdx.app.log("SocketHandler", "answerCount: $message")
        }
        mSocket.on("nextRound") { args ->
            val message = args[0] as JSONObject
            Gdx.app.log("SocketHandler", "nextRound: $message")
        }
        mSocket.on("endScore") { args ->
            val message = args[0] as JSONObject
            Gdx.app.log("SocketHandler", "endScore: $message")
        }
    }

    @Synchronized
    fun emit(event: String, data: JSONObject) {
        Gdx.app.log("SocketHandler", "Emitting event: $event with data: $data")
        mSocket.emit(event, data)
    }

    @Synchronized
    fun establishConnection() {
        mSocket.connect()
        while(!mSocket.connected()) {
        }
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }
}