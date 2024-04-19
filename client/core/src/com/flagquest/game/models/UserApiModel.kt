package com.flagquest.game.models

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.async.ThreadUtils
import com.flagquest.game.states.MainMenuState
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
            Gdx.app.log("UserApiModel", "Email or password cannot be empty")
            callback(false)
        }
        Gdx.app.log("UserApiModel", "Attempting to login with email: $email")

        try{
        authHandler.signIn(email, password) { success, uid, message ->
            if (success) {
                Gdx.app.log("UserApiModel", "Firebase auth successful")
                Gdx.app.log("UserApiModel", "Firebase User ID: $uid")

                GlobalScope.launch(Dispatchers.IO) {
                    val user = withContext(Dispatchers.Default) {
                        getUserByFirebaseId(uid!!)
                    }
                    val loggedIn = if (user != null) {
                        val userId: String = getIdFromResponse(user)
                        Gdx.app.log("UserApiModel", "User ID: $userId")
                        DataManager.setData("userId", userId)
                        true
                    } else {
                        Gdx.app.error("UserApiModel", "Error retrieving user")
                        false
                    }
                    callback(loggedIn)
                }

            } else {
                Gdx.app.log("UserApiModel", "Firebase auth failed: $message")
                callback(false)
            }
        }
        } catch (e: Exception) {
            Gdx.app.error("UserApiModel", "Error: ${e.message}")
            callback(false)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun registerUser(authHandler: AuthHandler, email: String, password: String, userName: String, callback: (Boolean)-> Unit) {
        if (email.isEmpty() || password.isEmpty()) {
            Gdx.app.log("RegistrationState", "Email or password cannot be empty")
            callback(false)
        }
        Gdx.app.log("RegistrationState", "Attempting to register with email: $email")
        try {
            authHandler.signUp(email, password) { success, uid, message ->
                if (success) {
                    Gdx.app.log("RegistrationState", "Registration successful")
                    Gdx.app.log("RegistrationState", "Firebase User ID: $uid")
                    GlobalScope.launch(Dispatchers.IO) {
                        val user: String? = withContext(Dispatchers.Default) {
                            // TODO: Remove hardcoded nationality and get it from the form
                            postUser(userName, "German", uid!!)
                        }
                        val registered = if (user != null) {
                            val userId: String = getIdFromResponse(user)
                            Gdx.app.log("RegistrationState", "User ID: $userId")
                            DataManager.setData("userId", userId)
                            true
                        } else {
                            Gdx.app.error("RegistrationState", "Error registering user")
                            false
                        }
                        callback(registered)
                    }
                } else {
                    Gdx.app.log("RegistrationState", "Registration failed: $message")
                    callback(false)
                }
            }
        } catch (e: Exception) {
            Gdx.app.error("RegistrationState", "Error: ${e.message}")
            callback(false)
        }
    }


    fun postUser(username: String, nationality: String, firebaseId: String): String? {
        val client = OkHttpClient()
        val mediaType = "application/json".toMediaType()
        val body = "{\r\n    \"userName\": \"$username\",\r\n    \"nationality\": \"$nationality\",\r\n    \"firebaseId\": \"$firebaseId\"\r\n}".toRequestBody(mediaType)
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/user/")
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("X-API-Key", "{{token}}")
            .build()
        val response = client.newCall(request).execute()

        val responseBodyString = response.body?.string() // Store the response body
        println(responseBodyString)
        return if(response.isSuccessful) {
            responseBodyString
        } else {
            println("Error: ${response.code} - ${response.message}")
            null
        }
    }

    /**
     * Function sends GET request to retrieve UserId with provided username
     * @param username of user
     * @return Server's response as a string
     */
    fun getUserByName(username: String): String? {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/user/byName/$username")
            .addHeader("X-API-Key", "{{token}}")
            .build()
        val response = client.newCall(request).execute()
        val responseBodyString = response.body?.string() // Store the response body
        println(responseBodyString)
        return if(response.isSuccessful) {
            responseBodyString
        } else {
            println("Error: ${response.code} - ${response.message}")
            null
        }
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
        val responseBodyString = response.body?.string() // Store the response body
        println(responseBodyString)
        return if(response.isSuccessful) {
            responseBodyString
        } else {
            println("Error: ${response.code} - ${response.message}")
            null
        }
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
        val responseBodyString = response.body?.string() // Store the response body
        println(responseBodyString)
        return if(response.isSuccessful) {
            responseBodyString
        } else {
            println("Error: ${response.code} - ${response.message}")
            null
        }
    }

    /**
     * Function sends PUT request to send friend request to user with provided friendId
     * @param friendId userId of friend
     * @return Server's response as string
     */
    fun putAddFriend(friendId: String): String? {
        val client = OkHttpClient()
        val mediaType = "text/plain".toMediaType()
        val body = "".toRequestBody(mediaType)
        val userId = DataManager.getData("userId") as String
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/user/$userId/friends/$friendId")
            .put(body)
            .addHeader("X-API-Key", "{{token}}")
            .build()
        val response = client.newCall(request).execute()
        val responseBodyString = response.body?.string() // Store the response body
        println(responseBodyString)
        return if(response.isSuccessful) {
            responseBodyString
        } else {
            println("Error: ${response.code} - ${response.message}")
            null
        }
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
    fun getFriendNames(): MutableList<String> {
        val friendNames = mutableListOf<String>()
        val user = getUserById(DataManager.getData("userId")!! as String)
        val friendUuidList = extractFriendUuidList(user!!)
        for (friendUuid in friendUuidList) {
            val friend = JSONObject(getUserById(friendUuid))
            friendNames.add(friend.getString("userName"))
        }
        return friendNames
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

    /**
     * Function retrieves ID from user object
     * @param responseBody String of user object
     * @return ID of user
     */
    fun getIdFromResponse(responseBody: String): String {
        val jsonObject = JSONObject(responseBody)
        return jsonObject.getString("id")
    }


    /**
     * Function extracts friendUuidList from user object
     * @param jsonString String of user object
     * @return List of friendUuids
     */
    private fun extractFriendUuidList(jsonString: String): List<String> {
        val jsonObject = JSONObject(jsonString)
        val friendUuidJsonArray = jsonObject.getJSONArray("friendUuidList")
        val friendUuidList = mutableListOf<String>()

        for (i in 0 until friendUuidJsonArray.length()) {
            friendUuidList.add(friendUuidJsonArray.getString(i))
        }

        return friendUuidList
    }

    /**
     * Function sends GET request to retrieve pending friend requests
     * @return List of pairs with usernames and userIds of pending friend requests
     */
    fun getPendingFriendRequests(): MutableList<Pair<String, String>> {
        val pendingFriendRequests = mutableListOf<Pair<String, String>>()
        val jsonUser = JSONObject(getUserById(DataManager.getData("userId")!! as String))
        val friendRequestIds = jsonUser.getJSONArray("pendingFriendRequests")
        for (i in 0 until friendRequestIds.length()) {
            val friendId = friendRequestIds.getString(i)
            val pendingFriendRequest = JSONObject(getUserById(friendId))
            pendingFriendRequests.add(pendingFriendRequest.getString("userName") to friendId)
        }
        return pendingFriendRequests
    }

    fun putAcceptRequest(friendId: String): String? {
        println("1")
        val client = OkHttpClient()
        println("2")
        val mediaType = "text/plain".toMediaType()
        println("3")
        val body = "".toRequestBody(mediaType)
        println("4")
        val userId = DataManager.getData("userId") as String
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/user/$userId/friends/requests/$friendId")
            .put(body)
            .addHeader("X-API-Key", "{{token}}")
            .build()
        println("5")
        val response = client.newCall(request).execute()
        println("6")
        val responseBodyString = response.body?.string() // Store the response body
        println(responseBodyString)
        return if(response.isSuccessful) {
            responseBodyString
        } else {
            println("Error: ${response.code} - ${response.message}")
            null
        }
    }

    fun delRejectRequest(friendId: String): String? {
        val client = OkHttpClient()
        val mediaType = "text/plain".toMediaType()
        val body = "".toRequestBody(mediaType)
        val userId = DataManager.getData("userId") as String
        val request = Request.Builder()
            .url("http://flagquest.leotm.de:3000/user/$userId/friends/requests/$friendId")
            .method("DELETE", body)
            .addHeader("X-API-Key", "{{token}}")
            .build()
        val response = client.newCall(request).execute()
        val responseBodyString = response.body?.string() // Store the response body
        println(responseBodyString)
        return if(response.isSuccessful) {
            responseBodyString
        } else {
            println("Error: ${response.code} - ${response.message}")
            null
        }
    }

    fun getResults(): MutableList<Pair<String, Int>> {
        // TODO: Should return a list of scores from players in the game
        return listOf(
            "amelmd" to 170,
            "BABA" to 240,
            "Cecilia" to 110,
            "Donald" to 190,
            "Emilie" to 40,
            "amelmd" to 170,
            "BABA" to 240,
            "Cecilia" to 110,
            "Donald" to 190,
            "Emilie" to 40,
            "amelmd" to 170,
            "BABA" to 240,
            "Cecilia" to 110,
            "Donald" to 190,
            "Emilie" to 40
        ) as MutableList<Pair<String, Int>>
    }
}