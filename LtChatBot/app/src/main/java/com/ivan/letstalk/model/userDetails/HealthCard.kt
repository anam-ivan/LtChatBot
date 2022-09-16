package com.ivan.letstalk.model.userDetails

import com.google.gson.annotations.SerializedName
import com.ivan.letstalk.model.userDetails.Progressive


data class HealthCard(
    @SerializedName("Progressive") var Progressive: ArrayList<Progressive> = arrayListOf(),
    @SerializedName("Transactional") var Transactional: ArrayList<String> = arrayListOf()
)