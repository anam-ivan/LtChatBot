package com.ivan.letstalk.model.userDetails

import com.google.gson.annotations.SerializedName


data class Gen(
  @SerializedName("name") var name: String? = null,
  @SerializedName("value") var value: ArrayList<Value> = arrayListOf()
)