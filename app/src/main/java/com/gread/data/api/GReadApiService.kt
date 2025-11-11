package com.gread.data.api

import com.gread.data.models.*
import retrofit2.http.*

interface GReadApiService {
    // Auth
    @POST("jwt-auth/v1/token")
    suspend fun login(@Body request: LoginRequest): JWTResponse

    @POST("wp/v2/users/register")
    suspend fun register(@Body request: RegisterRequest): User

    // User
    @GET("buddypress/v1/members/{id}")
    suspend fun getUser(@Path("id") userId: Int): User

    @GET("buddypress/v1/members/{id}/profile")
    suspend fun getUserStats(@Path("id") userId: Int): UserStats

    // Library
    @GET("gread/v1/library")
    suspend fun getLibrary(@Query("page") page: Int = 1, @Query("per_page") perPage: Int = 20): APIListResponse<Book>

    @POST("gread/v1/library/add")
    suspend fun addToLibrary(@Body request: AddToLibraryRequest): Book

    @POST("gread/v1/library/remove")
    suspend fun removeFromLibrary(@Body request: RemoveFromLibraryRequest): Map<String, Any>

    @POST("gread/v1/library/progress")
    suspend fun updateProgress(@Body request: UpdateProgressRequest): Book

    // Activity
    @GET("gread/v1/activity")
    suspend fun getActivityFeed(@Query("page") page: Int = 1, @Query("per_page") perPage: Int = 20): ActivityFeed

    // Search
    @GET("gread/v1/books/search")
    suspend fun searchBooks(@Query("q") query: String, @Query("page") page: Int = 1): APIListResponse<Book>

    // User Actions
    @POST("gread/v1/user/block")
    suspend fun blockUser(@Body request: BlockUserRequest): Map<String, Any>

    @POST("gread/v1/user/unblock")
    suspend fun unblockUser(@Body request: BlockUserRequest): Map<String, Any>

    @POST("gread/v1/user/mute")
    suspend fun muteUser(@Body request: BlockUserRequest): Map<String, Any>

    @POST("gread/v1/user/unmute")
    suspend fun unmuteUser(@Body request: BlockUserRequest): Map<String, Any>

    @GET("gread/v1/user/blocked_list")
    suspend fun getBlockedUsers(): List<User>

    @GET("gread/v1/user/muted_list")
    suspend fun getMutedUsers(): List<User>

    // Groups
    @GET("buddypress/v1/groups")
    suspend fun getGroups(@Query("page") page: Int = 1, @Query("per_page") perPage: Int = 20): APIListResponse<Group>
}

data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

data class AddToLibraryRequest(
    val isbn: String? = null,
    val title: String,
    val author: String,
    val pages: Int? = null
)

data class RemoveFromLibraryRequest(
    val book_id: Int
)

data class UpdateProgressRequest(
    val book_id: Int,
    val current_page: Int
)

data class BlockUserRequest(
    val user_id: Int
)

data class APIListResponse<T>(
    val items: List<T>? = null,
    val page: Int? = null,
    val per_page: Int? = null,
    val total: Int? = null
)
