package com.ivan.letstalk.model.markedAlkStatusChange

import com.google.gson.annotations.SerializedName

object MarkedAlkStatusChangePayload {
    data class MarkedAlkStatusChange(
        @SerializedName("m_id") val m_id: String,
        @SerializedName("status") val status: String,
    )
}