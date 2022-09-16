package com.ivan.letstalk.model.profile

import com.google.gson.annotations.SerializedName

data class PatientProfileResponse(
    @SerializedName("data") var data: ArrayList<PatientData>? = arrayListOf(),
    @SerializedName("message") var message: String? = null,
    @SerializedName("status") var status: String? = null
)