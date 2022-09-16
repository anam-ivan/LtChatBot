package com.ivan.letstalk.model.markedAlkSideEffects

import com.google.gson.annotations.SerializedName

data class MarkedAlkSideEffectsResponse (
    @SerializedName("count") val count : Int,
    @SerializedName("data") val data : List<Data>,
    @SerializedName("draw") val draw : Int,
    @SerializedName("limit") val limit : Int,
    @SerializedName("message") val message : String,
    @SerializedName("page") val page : Int,
    @SerializedName("status") val status : String,
    @SerializedName("totalDocs") val totalDocs : Int,
    @SerializedName("totalPages") val totalPages : Int
)