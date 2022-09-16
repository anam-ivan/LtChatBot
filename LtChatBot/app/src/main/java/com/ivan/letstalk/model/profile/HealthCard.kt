package com.ivan.letstalk.model.profile

import com.google.gson.annotations.SerializedName


data class HealthCard(

  @SerializedName("name") var name: String? = null,
  @SerializedName("value") var value: String? = null,
  @SerializedName("value_details") var valueDetails: String? = null

)