package com.ivan.letstalk.model.userDetails

import com.google.gson.annotations.SerializedName


data class ProfileResponse (
  @SerializedName("data" ) var data    : ArrayList<Data> = arrayListOf(),
  @SerializedName("message") var message : String? = null,
  @SerializedName("status") var status  : String? = null
)