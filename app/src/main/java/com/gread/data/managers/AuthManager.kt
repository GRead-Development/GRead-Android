package com.gread.data.managers

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.gread.data.api.GReadApiService
import com.gread.data.api.LoginRequest
import com.gread.data.models.JWTResponse
import com.gread.data.models.User
import kotlinx.coroutines.flow.Flow
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
            if (response.success && response.token != null && response.user != null) {
                context.authDataStore.edit { prefs ->
                    prefs[TOKEN_KEY] = response.token
                    prefs[USER_ID_KEY] = response.user.id.toString()
                    prefs[USERNAME_KEY] = response.user.username
                }
                Result.success(response)
            } else {
                Result.failure(Exception(response.message ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        context.authDataStore.edit { it.clear() }
    }

    suspend fun getToken(): String? {
        return context.authDataStore.data.map { it[TOKEN_KEY] }.map { it }.firstOrNull()
    }

    suspend fun isLoggedIn(): Boolean = getToken() != null
}
