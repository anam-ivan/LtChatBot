package com.ivan.letstalk.model.slug

import com.google.gson.annotations.SerializedName


data class Data(
  @SerializedName("_id") var Id: String? = null,
  @SerializedName("created_at") var createdAt: String? = null,
  @SerializedName("description") var description: String? = null,
  @SerializedName("files") var files: String? = null,
  @SerializedName("slug") var slug: String? = null,
  @SerializedName("status") var status: String? = null,
  @SerializedName("title") var title: String? = null,
  @SerializedName("updated_at") var updatedAt: String? = null
)