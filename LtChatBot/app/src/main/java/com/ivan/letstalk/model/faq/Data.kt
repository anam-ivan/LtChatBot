package com.ivan.letstalk.model.faq

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("_id") var Id: String? = null,
    @SerializedName("answer") var answer: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("question") var question: String? = null,
    @SerializedName("slug") var slug: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    var isExpanded: Boolean = false
)