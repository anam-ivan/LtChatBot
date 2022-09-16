package com.ivan.letstalk.model.chatHistoryTitle

import com.google.gson.annotations.SerializedName

object ChatHistoryTitlePayload {
    data class ChatHistoryTitle(
        @SerializedName("limit") val limit: Int,
        @SerializedName("page_no") val page_no: Int,
        @SerializedName("perpage") val perpage: Int,
        @SerializedName("filter_date") val filter_date: String,
        @SerializedName("user_id") val user_id: String,
        @SerializedName("status") val status: String
    )
}