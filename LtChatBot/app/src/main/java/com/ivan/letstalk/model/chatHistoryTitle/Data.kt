package com.ivan.letstalk.model.chatHistoryTitle

import com.google.gson.annotations.SerializedName

data class Data(
	@SerializedName("_id") val _id: String,
	@SerializedName("chat_id") val chat_id: String,
	@SerializedName("cr_no") val cr_no: Int,
	@SerializedName("end_date") val end_date: String,
	@SerializedName("name") val name: String,
	@SerializedName("start_date") val start_date: String,
	@SerializedName("status") val status: String,
	@SerializedName("user_id") val user_id: String
)