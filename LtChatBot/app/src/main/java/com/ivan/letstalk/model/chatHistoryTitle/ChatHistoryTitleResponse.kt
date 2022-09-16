package com.ivan.letstalk.model.chatHistoryTitle

import com.google.gson.annotations.SerializedName

data class ChatHistoryTitleResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("data") val data: List<Data>,
    @SerializedName("limit") val limit: Int,
    @SerializedName("message") val message: String,
    @SerializedName("page_no") val page_no: Int,
    @SerializedName("status") val status: String,
    @SerializedName("totalDocs") val totalDocs: Int,
    @SerializedName("totalPages") val totalPages: Int
)