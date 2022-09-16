package com.ivan.letstalk.model.topAlkSideEffects

import com.google.gson.annotations.SerializedName
import com.ivan.letstalk.model.faq.SortBy

object AlkSideEffectsPayload {
    data class AlkSideEffects(
        @SerializedName("draw") val draw: Int,
        @SerializedName("limit") val limit: Int,
        @SerializedName("page_no") val page_no: Int,
        @SerializedName("perpage") val perpage: Int,
        @SerializedName("search_key") val search_key: String,
        @SerializedName("sort_by") val sort_by: SortBy
    )
}