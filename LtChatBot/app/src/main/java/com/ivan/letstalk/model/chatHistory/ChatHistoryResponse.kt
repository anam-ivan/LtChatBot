package com.ivan.letstalk.model.chatHistory

import com.google.gson.annotations.SerializedName

data class ChatHistoryResponse (
    @SerializedName("data") val data : Data,
    @SerializedName("message") val message : String,
    @SerializedName("status") val status : String
)