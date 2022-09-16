package com.ivan.letstalk.model.profile

import com.google.gson.annotations.SerializedName


data class Medicine(

  @SerializedName("present") var present: ArrayList<Present> = arrayListOf(),
  @SerializedName("previous") var previous: ArrayList<String> = arrayListOf()

)