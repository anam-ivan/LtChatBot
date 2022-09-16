package com.ivan.letstalk.model.profile

import com.google.gson.annotations.SerializedName

data class PatientData (
    @SerializedName("basic_details") var basicDetails : BasicDetails? = BasicDetails(),
    @SerializedName("health_card") var healthCard   : ArrayList<HealthCard> = arrayListOf(),
    @SerializedName("medicine") var medicine     : ArrayList<Medicine>  = arrayListOf(),
    @SerializedName("side_effects") var sideEffects  : ArrayList<SideEffects> = arrayListOf()
)