package com.ivan.letstalk.model.sideEffectsHistoryResponse

import com.google.gson.annotations.SerializedName

data class SideEffectHistoryResponse(
    @SerializedName("data") var data: ArrayList<Data> = arrayListOf(),
    @SerializedName("message") var message: String? = null,
    @SerializedName("status") var status: String? = null
)