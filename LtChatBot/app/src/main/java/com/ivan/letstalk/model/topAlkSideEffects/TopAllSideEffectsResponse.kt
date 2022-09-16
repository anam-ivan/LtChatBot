package com.ivan.letstalk.model.topAlkSideEffects

import com.google.gson.annotations.SerializedName


data class TopAllSideEffectsResponse (
	@SerializedName("count") val count : Int,
	@SerializedName("data") val data : List<Data>,
	@SerializedName("message") val message : String,
	@SerializedName("page_no") val page_no : Int,
	@SerializedName("status") val status : String
)