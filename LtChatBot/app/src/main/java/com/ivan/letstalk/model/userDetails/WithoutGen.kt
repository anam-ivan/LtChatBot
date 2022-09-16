package com.ivan.letstalk.model.userDetails

import com.example.example.Info
import com.google.gson.annotations.SerializedName


data class WithoutGen(
  @SerializedName("code") var code: String? = null,
  @SerializedName("info") var info: Info? = Info(),
  @SerializedName("name") var name: String? = null,
  @SerializedName("value") var value: String? = null,
  @SerializedName("value_details") var valueDetails: String? = null
)