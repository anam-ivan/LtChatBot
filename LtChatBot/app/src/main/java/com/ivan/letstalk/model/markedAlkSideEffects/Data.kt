package com.ivan.letstalk.model.markedAlkSideEffects

import com.google.gson.annotations.SerializedName


data class Data(
	@SerializedName("_id") val _id: String,
	@SerializedName("created_at") val created_at: String,
	@SerializedName("status") val status: String,
	@SerializedName("tas_id") val tas_id: String,
	@SerializedName("top_alk_details") val top_alk_details: TopAlkDetails,
	@SerializedName("updated_at") val updated_at: String,
	@SerializedName("user_id") val user_id: String
)