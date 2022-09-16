package com.ivan.letstalk.model.doctorsAssigned

import com.google.gson.annotations.SerializedName

data class DoctorsAssignedForPatientsResponse(
    @SerializedName("count") var count: Int? = null,
    @SerializedName("data") var data: ArrayList<Data> = arrayListOf(),
    @SerializedName("message") var message: String? = null,
    @SerializedName("status") var status: String? = null,
)