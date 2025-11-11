package com.gread.data.models

import com.google.gson.annotations.SerializedName

data class Group(
    val id: Int,
    val name: String,
    val slug: String,
    val description: String = "",
    val avatar: String = "",
    val cover: String = "",
    @SerializedName("total_member_count")
    val totalMemberCount: Int = 0,
    @SerializedName("date_created")
    val dateCreated: String,
    val status: String = "public"
)
