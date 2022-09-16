package com.ivan.letstalk.model.profile

import com.google.gson.annotations.SerializedName
import com.ivan.letstalk.model.profile.Present


data class SideEffects(
    @SerializedName("present") var present: ArrayList<Present> = arrayListOf(),
    @SerializedName("previous") var previous: ArrayList<String> = arrayListOf()
)