package com.ivan.letstalk.model.documents

import com.google.gson.annotations.SerializedName

data class AddDocumentUploadResponse(
    @SerializedName("data") var data: Data? = Data(),
    @SerializedName("message") var message: String? = null,
    @SerializedName("status") var status: String? = null
)