package com.ivan.letstalk.model.documents

import com.google.gson.annotations.SerializedName
import com.ivan.letstalk.model.faq.SortBy

object DocumentsPayload {
    data class Documents(
        @SerializedName("user_id") val user_id: String,
        @SerializedName("draw") val draw: Int,
        @SerializedName("limit") val limit: Int,
        @SerializedName("page_no") val page_no: Int,
        @SerializedName("perpage") val perpage: Int,
        @SerializedName("search_key") val search_key: String,
        @SerializedName("sort_by") val sort_by: SortBy
    )
}