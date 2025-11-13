package com.gread.data.models

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    @SerializedName("user_login")
    val username: String = "",
    @SerializedName("user_email")
    val email: String = "",
    @SerializedName("display_name")
    val displayName: String = "",
    @SerializedName("user_registered")
    val userRegistered: String = "",
    val avatar: String = "",
    @SerializedName("xprofile")
    val profile: Map<String, Any>? = null
)

data class JWTResponse(
    val success: Boolean = true,
    val token: String? = null,
    val user: User? = null,
    val message: String? = null
)

data class LoginResponse(
    val token: String,
    val user_id: Int,
    val user_email: String,
    val user_nicename: String,
    val user_display_name: String
)

data class UserStats(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("books_read")
    val booksRead: Int,
    @SerializedName("pages_read")
    val pagesRead: Int,
    @SerializedName("total_points")
    val totalPoints: Int,
    @SerializedName("current_streak")
    val currentStreak: Int,
    val achievements: List<String> = emptyList()
)
