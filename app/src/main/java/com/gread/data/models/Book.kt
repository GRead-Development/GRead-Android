package com.gread.data.models

import com.google.gson.annotations.SerializedName

data class Book(
    val id: Int,
    val title: String,
    val author: String = "",
    val isbn: String = "",
    val cover: String = "",
    val pages: Int = 0,
    @SerializedName("publication_date")
    val publicationDate: String = "",
    val description: String = "",
    @SerializedName("current_page")
    val currentPage: Int = 0,
    @SerializedName("date_added")
    val dateAdded: String = "",
    @SerializedName("date_finished")
    val dateFinished: String? = null,
    val status: String = "reading", // reading, completed, wishlist
    val tags: List<String> = emptyList()
)

data class BookLibrary(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("book_id")
    val bookId: Int,
    val status: String = "reading",
    @SerializedName("current_page")
    val currentPage: Int = 0,
    @SerializedName("date_added")
    val dateAdded: String = "",
    @SerializedName("date_started")
    val dateStarted: String? = null,
    @SerializedName("date_finished")
    val dateFinished: String? = null
)
