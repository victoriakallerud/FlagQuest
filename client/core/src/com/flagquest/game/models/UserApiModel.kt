package com.flagquest.game.models

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.async.ThreadUtils
import com.flagquest.game.utils.DataManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

/**
 * Class handles API requests associated with User
 */
class UserApiModel {
    private val level: String = "Europe" // TODO Decide if we want to have Europe or All as our level

/** Function verifies the users credentials using the firebase auth module and retrieves a matching user from the database
     * @param authHandler: AuthHandler object
     * @param email: Users email
     * @param password: Users password
     * @param callback: Returns a boolean value to indicate if the login was successful
     */
    @OptIn(DelicateCoroutinesApi::class)
    fun loginUser(authHandler: AuthHandler, email: String, password: String, callback: (Boolean)-> Unit) {
        if (email.isEmpty() || password.isEmpty()) {
            Gdx.app.log("LoginState", "Email or password cannot be empty")
            callback(false)
        }
        Gdx.app.log("LoginState", "Attempting to login with email: $email")

        try{
        authHandler.signIn(email, password) { success, uid, message ->
            if (success) {
                Gdx.app.log("LoginState", "Firebase auth successful")
                Gdx.app.log("LoginState", "Firebase User ID: $uid")

                GlobalScope.launch(Dispatchers.IO) {
                    val user = withContext(Dispatchers.Default) {
                        getUserByFirebaseId(uid!!)
                    }
                    val loggedIn = if (user != null) {
                        val userId: String = getIdFromResponse(user)
                        Gdx.app.log("LoginState", "User ID: $userId")
                        DataManager.setData("userId", userId)
                        true
                    } else {
                        Gdx.app.error("LoginState", "Error retrieving user")
                        false
                    }
                    callback(loggedIn)
                }

            } else {
                Gdx.app.log("LoginState", "Firebase auth failed: $message")
                callback(false)
            }
        }
        } catch (e: Exception) {
            Gdx.app.error("LoginState", "Error: ${e.message}")
            callback(false)
        }
    }

    fun postUser(name: String, username: String, nationality: String, password: String): String? {
        val client = OkHttpClient()
        val mediaType = "application/json".toMediaType()
        val body = "{\r\n    \"userName\": \"$username\",\r\n    \"nationality\": \"$nationality\"\r\n}".toRequestBody(mediaType)
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/user/USER_ID")
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("X-API-Key", "{{token}}")
            .build()
        val response = client.newCall(request).execute()
        println(response.body?.string())
        return response.body?.string()
    }

    fun getUserByName(username: String): String? {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/user/byName/$username")
            .addHeader("X-API-Key", "{{token}}")
            .build()
        val response = client.newCall(request).execute()
        println(response.body?.string())
        return response.body?.string()
    }

    fun getUserByFirebaseId(firebaseId: String): String? {
        Gdx.app.log("UserApiModel", "Attempting to retrieve user with firebaseId: $firebaseId")
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/user/byFirebaseId/$firebaseId")
            .addHeader("X-API-Key", "{{token}}")
            .build()
        val response = client.newCall(request).execute()
        Gdx.app.log("UserApiModel", "Executed request...")
        val responseBody = response.body?.string()
        println(responseBody)
        // Return lobby JSON response if successful, otherwise return null
        return if (response.isSuccessful) {
            responseBody
        } else {
            println("Error: ${response.code} - ${response.message}")
            null
        }
    }


    /**
     * Function sends GET request to retrieve User with provided userId
     * @param id of user
     * @return Server's response as a string
     */
    fun getUserById(id: String): String? {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/user/$id")
            .addHeader("X-API-Key", "{{token}}")
            .build()
        val response = client.newCall(request).execute()
        return response.body?.string()
    }


    /**
     * Function sends GET request to retrieve users with top 10 scores
     * @return Server's response as string
     */
    fun getTopTenUsers(): String? {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/user/highScores/$level/GuessingFlags")
            .addHeader("Content-Type", "application/json")
            .addHeader("X-API-Key", "{{token}}")
            .build()
        val response = client.newCall(request).execute()
        return response.body?.string()
    }

    /**
     * Function takes in list of highscores and creates list with pairs of username and score
     * @return List with pairs of username and score
     */
    fun parseHighscores(highscoreString: String?): List<Pair<String, Int>> {
        val highscoreArray = JSONArray(highscoreString!!)
        val highscores = mutableListOf<Pair<String, Int>>()
        println(highscoreArray)

        for (i in 0 until highscoreArray.length()) {
            val jsonObject = highscoreArray.getJSONObject(i)
            val userName = jsonObject.getString("userName")
            val score = jsonObject.getInt("score")
            highscores.add(userName to score)
        }

        return highscores
    }

    /**
     * Function retrieves highscores of top 10 friends
     * @return List with pairs of username and score
     */
    fun getFriendHighscores(): MutableList<Pair<String, Int>> {
        val friendsScore = mutableListOf<Pair<String, Int>>()
        val user = getUserById(DataManager.getData("userId")!! as String)
        val friendUuidList = extractFriendUuidList(user!!)

        for (friendUuid in friendUuidList) {
            val friend = JSONObject(getUserById(friendUuid))
            val highScoresArray = friend.optJSONArray("highScores")
            if (highScoresArray != null) {
                for (i in 0 until highScoresArray.length()) {
                    val scoreObject = highScoresArray.getJSONObject(i)
                    val scoreLevel = scoreObject.optString("level")
                    if (scoreLevel == level) {
                        val username: String = friend.optString("userName")
                        val userscore: Int = scoreObject.optInt("value")
                        friendsScore.add(username to userscore)
                    }
                }
            }
        }
        return friendsScore
    }


    fun getIdFromResponse(responseBody: String): String {
        val jsonObject = JSONObject(responseBody)
        return jsonObject.getString("id")
    }


    fun extractFriendUuidList(jsonString: String): List<String> {
        val jsonObject = JSONObject(jsonString)
        val friendUuidJsonArray = jsonObject.getJSONArray("friendUuidList")
        val friendUuidList = mutableListOf<String>()

        for (i in 0 until friendUuidJsonArray.length()) {
            friendUuidList.add(friendUuidJsonArray.getString(i))
        }

        return friendUuidList
    }
}