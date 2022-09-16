package com.ivan.letstalk.model.documents

import com.google.gson.annotations.SerializedName

object DocumentUploadPayload {
    data class Document(
        @SerializedName("document_category_id") var document_category_id: String? = null,
        @SerializedName("document_name") var document_name: String? = null,
        @SerializedName("record_date") var record_date: String? = null,
        @SerializedName("record_time") var record_time: String? = null
    )
}