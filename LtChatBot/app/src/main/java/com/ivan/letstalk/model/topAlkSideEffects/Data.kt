package com.ivan.letstalk.model.topAlkSideEffects

import com.google.gson.annotations.SerializedName


data class Data(
	@SerializedName("_id") val _id: String,
	@SerializedName("author") val author: String,
	@SerializedName("created_at") val created_at: String,
	@SerializedName("description") val description: String,
	@SerializedName("status") val status: String,
	@SerializedName("title") val title: String,
	@SerializedName("updated_at") val updated_at: String
)