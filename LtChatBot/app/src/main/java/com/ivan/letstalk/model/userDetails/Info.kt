package com.example.example

import com.google.gson.annotations.SerializedName
import com.ivan.letstalk.model.userDetails.Conditions


data class Info (

    @SerializedName("conditions" ) var conditions : Conditions? = Conditions(),
    @SerializedName("input_type" ) var inputType  : String?     = null,
    @SerializedName("meta"       ) var meta       : String?     = null,
    @SerializedName("org_name"   ) var orgName    : String?     = null,
    @SerializedName("type"       ) var type       : String?     = null

)