package com.gread.data.managers

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.gread.data.api.GReadApiService
import com.gread.data.api.LoginRequest
import com.gread.data.models.JWTResponse
import com.gread.data.models.LoginResponse
import com.gread.data.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val AUTH_STORE = "auth_store"
private val TOKEN_KEY = stringPreferencesKey("token")
private val USER_ID_KEY = stringPreferencesKey("user_id")
private val USERNAME_KEY = stringPreferencesKey("username")

private val Context.authDataStore by preferencesDataStore(AUTH_STORE)

class AuthManager(
    private val context: Context,
    private val apiService: GReadApiService
) {
    val tokenFlow: Flow<String?> = context.authDataStore.data.map { it[TOKEN_KEY] }
    val userIdFlow: Flow<String?> = context.authDataStore.data.map { it[USER_ID_KEY] }
    val usernameFlow: Flow<String?> = context.authDataStore.data.map { it[USERNAME_KEY] }

    suspend fun login(username: String, password: String): Result<JWTResponse> {
        return try {
            val response = apiService.login(LoginRequest(username, password))
            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null && loginResponse.token.isNotEmpty()) {
                    // Convert LoginResponse to User object
                    val user = User(
                        id = loginResponse.user_id,
                        username = loginResponse.user_nicename,
                        email = loginResponse.user_email,
                        displayName = loginResponse.user_display_name,
                        userRegistered = "",
                        avatar = ""
                    )

                    // Store token and user data
                    context.authDataStore.edit { prefs ->
                        prefs[TOKEN_KEY] = loginResponse.token
                        prefs[USER_ID_KEY] = loginResponse.user_id.toString()
                        prefs[USERNAME_KEY] = loginResponse.user_nicename
                    }

                    // Return JWTResponse format for consistency
                    val jwtResponse = JWTResponse(
                        success = true,
                        token = loginResponse.token,
                        user = user
                    )
                    Result.success(jwtResponse)
                } else {
                    Result.failure(Exception("No token received"))
                }
            } else {
                // Handle error response - try to extract error message from body
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        val errorJson = com.google.gson.Gson().fromJson(errorBody, Map::class.java)
                        (errorJson["message"] as? String)?.replace("<strong>", "")?.replace("</strong>", "")
                            ?: "Login failed with status ${response.code()}"
                    } catch (e: Exception) {
                        "Login failed with status ${response.code()}"
                    }
                } else {
                    "Login failed with status ${response.code()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        context.authDataStore.edit { it.clear() }
    }

    suspend fun getToken(): String? {
        return try {
            context.authDataStore.data.first()[TOKEN_KEY]
        } catch (e: Exception) {
            null
        }
    }

    suspend fun isLoggedIn(): Boolean = getToken() != null
}
