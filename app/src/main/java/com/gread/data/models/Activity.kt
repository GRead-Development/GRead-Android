package com.gread.data.models

import com.google.gson.annotations.SerializedName

data class Activity(
    val id: Int,
    val type: String, // post, comment, activity_update, book_progress
    @SerializedName("user_id")
    val userId: Int,
    val content: String,
    @SerializedName("secondary_item_id")
    val secondaryItemId: Int? = null,
    @SerializedName("date_recorded")
    val dateRecorded: String,
    val user: User? = null,
    val book: Book? = null
)

data class ActivityFeed(
    val activities: List<Activity>,
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    val total: Int
)

data class Notification(
    val id: Int,
    val type: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("from_user_id")
    val fromUserId: Int,
    val content: String,
    @SerializedName("is_new")
    val isNew: Boolean,
    @SerializedName("date_recorded")
    val dateRecorded: String
)
