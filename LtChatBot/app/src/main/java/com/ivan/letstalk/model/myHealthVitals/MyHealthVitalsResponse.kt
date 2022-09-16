package com.ivan.letstalk.model.myHealthVitals

import com.google.gson.annotations.SerializedName
import com.ivan.letstalk.model.myHealthVitals.Data


data class MyHealthVitalsResponse(
    @SerializedName("data") var data: ArrayList<Data> = arrayListOf(),
    @SerializedName("message") var message: String? = null,
    @SerializedName("status") var status: String? = null
)