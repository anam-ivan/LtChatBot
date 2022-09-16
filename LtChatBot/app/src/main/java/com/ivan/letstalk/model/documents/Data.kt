package com.ivan.letstalk.model.documents

import com.google.gson.annotations.SerializedName


data class Data(
  @SerializedName("_id") var Id: String? = null,
  @SerializedName("created_at") var createdAt: String? = null,
  // @SerializedName("document_category_id") var documentCategoryId: String? = null,
  @SerializedName("document_category_name") var documentCategoryName: String? = null,
  @SerializedName("document_name") var document_name: String? = null,
  @SerializedName("record_date") var record_date: String? = null,
  @SerializedName("record_time") var record_time: String? = null,
  @SerializedName("status") var status: String? = null,
  @SerializedName("updated_at") var updatedAt: String? = null,
  @SerializedName("user_id") var user_id: String? = null
)