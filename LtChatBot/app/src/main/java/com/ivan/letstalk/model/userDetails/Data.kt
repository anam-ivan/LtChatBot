package com.ivan.letstalk.model.userDetails

import com.google.gson.annotations.SerializedName


data class Data(
    @SerializedName("basic_details") var basicDetails: BasicDetails? = BasicDetails(),
    @SerializedName("health_card") var healthCard: ArrayList<HealthCard> = arrayListOf()
)