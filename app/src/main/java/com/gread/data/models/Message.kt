package com.gread.data.models

import com.google.gson.annotations.SerializedName

data class Message(
    val id: Int,
    @SerializedName("sender_id")
    val senderId: Int,
    @SerializedName("recipient_id")
    val recipientId: Int,
    val subject: String,
    val message: String,
    @SerializedName("date_sent")
    val dateSent: String,
    val thread_id: Int? = null
)

data class Conversation(
    val userId: Int,
    val messages: List<Message>,
    @SerializedName("last_message_date")
    val lastMessageDate: String
)
