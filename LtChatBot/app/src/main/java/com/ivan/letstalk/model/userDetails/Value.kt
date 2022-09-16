package com.ivan.letstalk.model.userDetails

import com.example.example.Info
import com.google.gson.annotations.SerializedName


data class Value(
  @SerializedName("code") var code: String? = null,
  @SerializedName("info") var info: Info? = Info(),
  @SerializedName("line_of_treatment") var lineOfTreatment: String? = null,
  @SerializedName("value") var value: String? = null,
  @SerializedName("value_details") var valueDetails: String? = null
)