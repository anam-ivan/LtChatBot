package com.ivan.letstalk.model.documents

import com.google.gson.annotations.SerializedName

data class DocumentListResponse(
    @SerializedName("count") var count: Int? = null,
    @SerializedName("data") var data: ArrayList<MData> = arrayListOf(),
    @SerializedName("draw") var draw: String? = null,
    @SerializedName("limit") var limit: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("page") var page: Int? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("totalDocs") var totalDocs: Int? = null,
    @SerializedName("totalPages") var totalPages: Int? = null
)