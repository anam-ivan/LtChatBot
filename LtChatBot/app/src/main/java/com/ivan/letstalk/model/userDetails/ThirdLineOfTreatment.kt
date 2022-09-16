package com.ivan.letstalk.model.userDetails

import com.google.gson.annotations.SerializedName

data class ThirdLineOfTreatment (
    @SerializedName("name") var name : String? = null,
    @SerializedName("value") var value : String? = null,
    @SerializedName("value_details") var valueDetails : String? = null
)