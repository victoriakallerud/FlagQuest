package com.flagquest.game.models

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class LobbyApiModel {
    private val userId: String = "0e7cb4e7-c8db-41e7-b536-bf94c66c9e50"
    fun createLobby(size: Int): String? {
        val client = OkHttpClient()
        val mediaType = "application/json".toMediaType()
        val body = ("{" +
                "\r\n  \"name\": \"12345\"," + //
                "\r\n  \"admin\": \"$userId\"," + // TODO: Implement function to get user's id
                "\r\n  \"options\": " +
                "{\r\n    \"maxNumOfPlayers\": \"$size\"," +
                "\r\n    \"numberOfQuestions\": 10," +
                "\r\n    \"showAnswers\": true," +
                "\r\n    \"gameMode\": \"GuessingFlags\"," +
                "\r\n    \"level\": \"Europe\"," +
                "\r\n    \"isPrivate\": false" +
                "\r\n  }\r\n" +
                "}").toRequestBody(mediaType)
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/lobby/")
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("X-API-Key", "{{token}}")
            .build()
        val response = client.newCall(request).execute()
        return response.body?.string()
    }
}