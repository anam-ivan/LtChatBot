package com.ivan.letstalk.model.chatHistory

import com.google.gson.annotations.SerializedName


data class Conversation(
	@SerializedName("sender") val sender: String,
	@SerializedName("text") val text: String,
	@SerializedName("timestamp") val timestamp: String
)