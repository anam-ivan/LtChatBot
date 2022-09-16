package com.ivan.letstalk.model.deleteDocuments
import com.google.gson.annotations.SerializedName
data class Data(

	@SerializedName("_id") val _id: String,
	@SerializedName("status") val status: String
)