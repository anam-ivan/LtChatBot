package com.ivan.letstalk.model.slug

import com.google.gson.annotations.SerializedName

data class SlugResponse(
    @SerializedName("data") var data: Data? = Data(),
    @SerializedName("msg") var msg: String? = null,
    @SerializedName("status") var status: String? = null
)