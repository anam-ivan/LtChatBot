package com.ivan.letstalk.model.faq

import com.google.gson.annotations.SerializedName


data class SortBy(
  @SerializedName("key") var key: String? = null,
  @SerializedName("dir") var dir: String? = null
)