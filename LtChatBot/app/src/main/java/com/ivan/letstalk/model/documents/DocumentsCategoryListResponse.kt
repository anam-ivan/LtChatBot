package com.ivan.letstalk.model.documents

import com.google.gson.annotations.SerializedName

data class DocumentsCategoryListResponse(
    @SerializedName("data") var data: ArrayList<Data> = arrayListOf(),
    @SerializedName("message") var message: String? = null,
    @SerializedName("status") var status: String? = null
)