package com.flagquest.game.utils

import io.socket.client.IO
import io.socket.client.Socket

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