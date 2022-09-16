package com.ivan.letstalk.model.faq

import com.google.gson.annotations.SerializedName

data class FaqResponse(
    @SerializedName("count") var count: Int? = null,
    @SerializedName("data") var data: ArrayList<Data> = arrayListOf(),
    @SerializedName("draw") var draw: String? = null,
    @SerializedName("limit") var limit: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("page") var page: Int? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("totalDocs") var totalDocs: Int? = null,
    @SerializedName("totalPages") var totalPages: Int? = null
)