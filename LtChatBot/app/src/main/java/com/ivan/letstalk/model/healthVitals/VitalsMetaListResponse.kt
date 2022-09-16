package com.ivan.letstalk.model.healthVitals

import com.google.gson.annotations.SerializedName
import com.ivan.letstalk.model.healthVitals.Data


data class VitalsMetaListResponse(
    @SerializedName("data") var data: ArrayList<Data> = arrayListOf(),
    @SerializedName("message") var message: String? = null,
    @SerializedName("status") var status: String? = null
)