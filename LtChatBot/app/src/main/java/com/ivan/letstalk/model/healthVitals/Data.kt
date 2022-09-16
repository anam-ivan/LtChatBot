package com.ivan.letstalk.model.healthVitals

import com.google.gson.annotations.SerializedName


data class Data(
  @SerializedName("_id") var Id: String? = null,
  @SerializedName("create_date") var createDate: String? = null,
  @SerializedName("icon") var icon: String? = null,
  @SerializedName("name") var name: String? = null,
  @SerializedName("status") var status: String? = null,
  @SerializedName("unit") var unit: String? = null,
  @SerializedName("update_date") var updateDate: String? = null
)