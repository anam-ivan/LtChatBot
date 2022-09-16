package com.ivan.letstalk.model.markedAlkStatusChange

import com.google.gson.annotations.SerializedName

data class MarkedAlkStatusChangeResponse (
    @SerializedName("data") val data : Data,
    @SerializedName("message") val message : String,
    @SerializedName("status") val status : String
)