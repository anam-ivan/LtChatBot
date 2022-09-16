package com.ivan.letstalk.model.markedAlkStatusChange

import com.google.gson.annotations.SerializedName


data class Data(
	@SerializedName("m_id") val m_id: String,
	@SerializedName("status") val status: String
)