package com.flagquest.game.models

import com.flagquest.game.utils.DataManager
import com.flagquest.game.utils.SocketHandler
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject


/**
 * Class handles API requests associated with Lobby
 */
class LobbyApiModel {
    private val userId: String = DataManager.getData("userId") as String

    /**
     * Function sends GET request to retrieve lobby with lobbyId
     */
    fun getLobby(lobbyId: String): String? {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/lobby/${lobbyId}")
            .addHeader("X-API-Key", "{{token}}")
            .build()
        val response = client.newCall(request).execute()
        val responseBodyString = response.body?.string() // Store the response body
        println(responseBodyString)

        // Return lobby JSON response if successful, otherwise return null
        return if (response.isSuccessful) {
            responseBodyString
        } else {
            println("Error: ${response.code} - ${response.message}")
            null
        }
    }

    /**
     * Function sends POST request to create a new lobby
     * @param size Maximum number of players allowed in the lobby
     * @return Server's response as string in JSON format
     * @see getIdFromResponse
     */
    fun postLobby(size: Int): String? {
        val client = OkHttpClient()
        val mediaType = "application/json".toMediaType()
        val body = ("{" +
                "\r\n  \"name\": \"12345\"," +
                "\r\n  \"admin\": \"$userId\"," +
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
        val responseBodyString = response.body?.string() // Store the response body
        println(responseBodyString)

        // Return lobby JSON response if successful, otherwise return null
        return if (response.isSuccessful) {
            responseBodyString
        } else {
            println("Error: ${response.code} - ${response.message}")
            null
        }

    }

    fun leaveLobby(): String? {
        val client = OkHttpClient()
        val mediaType = "text/plain".toMediaType()
        val body = "".toRequestBody(mediaType)
        val userId = DataManager.getData("userId") as String
        val lobbyId = DataManager.getData("lobbyId") as String
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/lobby/$lobbyId/$userId")
            .method("DELETE", body)
            .addHeader("X-API-Key", "{{token}}")
            .build()
        val response = client.newCall(request).execute()
        val responseBodyString = response.body?.string() // Store the response body
        println(responseBodyString)
        return if(response.isSuccessful){
            DataManager.clearData("lobbyId")
            SocketHandler.closeConnection()
            SocketHandler.removeAllListeners()
            responseBodyString
        } else {
            println("Error: ${response.code} - ${response.message}")
            null
        }
    }

    /**
     * Function sends PUT request to join a lobby with an invite code
     * @param inviteCode six digit invite code
     * @return Server's response as string
     */
    fun putLobbyWithInviteCode(inviteCode: String): String? {
        val client = OkHttpClient()
        val mediaType = "text/plain".toMediaType()
        val body = "".toRequestBody(mediaType)
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/lobby/invite/$inviteCode/$userId") // lobbyId/userId TODO: Add functionality to use the device's user's ID
            .put(body)
            .addHeader("X-API-Key", "{{token}}")
            .build()
        val response = client.newCall(request).execute()
        val responseBodyString = response.body?.string() // Store the response body
        println(responseBodyString)

        // Return lobby JSON response if successful, otherwise return null
        return if (response.isSuccessful) {
            responseBodyString
        } else {
            println("Error: ${response.code} - ${response.message}")
            null
        }
    }

    /**
     * Function sends PUT request to join a lobby with a lobbyId
     */
    fun putLobbyWithId(id: String): String? {
        val client = OkHttpClient()
        val mediaType = "text/plain".toMediaType()
        val body = "".toRequestBody(mediaType)
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/lobby/$id/$userId" )// lobbyId/userId TODO: Add functionality to use the device's user's ID
            .put(body)
            .addHeader("X-API-Key", "{{token}}")
            .build()
        val response = client.newCall(request).execute()
        val responseBodyString = response.body?.string() // Store the response body
        println(responseBodyString)

        // Return lobby JSON response if successful, otherwise return null
        return if (response.isSuccessful) {
            responseBodyString
        } else {
            println("Error: ${response.code} - ${response.message}")
            null
        }
    }

    /**
     * Function sends GET request to retrieve all lobbies
     * @return Server's response as string
     */
    fun getAllLobbies(): String? {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/lobby/")
            .addHeader("X-API-Key", "{{token}}")
            .build()
        val response = client.newCall(request).execute()
        val responseBodyString = response.body?.string() // Store the response body
        println(responseBodyString)

        // Return lobby JSON response if successful, otherwise return null
        return if (response.isSuccessful) {
            responseBodyString
        } else {
            println("Error: ${response.code} - ${response.message}")
            null
        }
    }

    /**
     * Function extracts and returns a lobby's ID
     * @param responseBody Server's response as string in JSON format
     * @return Lobby's ID
     */
    fun getIdFromResponse(responseBody: String): String? {
        val jsonObject = JSONObject(responseBody)
        println("JsonObject: $jsonObject")
        println("ID: ${jsonObject.getString("id")}")
        return jsonObject.getString("id")
    }

    /**
     * Function extracts first lobby from lobby list
     * @param lobbyList List of all lobbies
     * @return First lobby in list
     */
    fun getFirstLobby(lobbyList: String): String? {
        val jsonArray = JSONArray(lobbyList)
        if (jsonArray.length() > 0) {
            val firstLobby = jsonArray.getJSONObject(0)
            return firstLobby.toString()
        }
        return null
    }
}

