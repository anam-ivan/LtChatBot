package com.ivan.letstalk.model.knowYourSideEffects

import com.google.gson.annotations.SerializedName
import com.ivan.letstalk.model.faq.SortBy

object KnowYourSideEffectsPayload {
    data class KnowYourSideEffects(
        @SerializedName("draw") val draw: Int,
        @SerializedName("limit") val limit: Int,
        @SerializedName("page_no") val page_no: Int,
        @SerializedName("perpage") val perpage: Int,
        @SerializedName("search_key") val search_key: String,
        @SerializedName("sort_by") val sort_by: SortBy
    )
}