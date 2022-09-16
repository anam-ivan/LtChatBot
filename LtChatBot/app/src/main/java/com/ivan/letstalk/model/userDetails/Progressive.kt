package com.ivan.letstalk.model.userDetails

import com.google.gson.annotations.SerializedName


data class Progressive(
    @SerializedName("gen") var gen: ArrayList<Gen> = arrayListOf(),
    @SerializedName("without_gen") var withoutGen: ArrayList<WithoutGen> = arrayListOf()
)