package com.gread.data.api

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

private const val AUTH_STORE = "auth_store"
private val TOKEN_KEY = stringPreferencesKey("token")
private val Context.authDataStore by preferencesDataStore(AUTH_STORE)

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        // Get the token from DataStore synchronously
        val token = try {
            runBlocking {
                context.authDataStore.data.first()[TOKEN_KEY]
            }
        } catch (e: Exception) {
            null
        }

        // Add Authorization header if token exists
        if (token != null) {
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        }

        return chain.proceed(request)
    }
}
