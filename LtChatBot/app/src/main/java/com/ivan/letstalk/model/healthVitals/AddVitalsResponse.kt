package com.ivan.letstalk.model.healthVitals

import com.google.gson.annotations.SerializedName

data class AddVitalsResponse(
    @SerializedName("message") var message: String? = null,
    @SerializedName("status") var status: String? = null
)