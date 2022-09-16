package com.ivan.letstalk.model.imageUpload

import com.google.gson.annotations.SerializedName

data class ImageUploadResponse(
    @SerializedName("data") var data: Data? = Data(),
    @SerializedName("message") var message: String? = null,
    @SerializedName("status") var status: String? = null
)