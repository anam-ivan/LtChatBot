package com.ivan.letstalk.model.deleteDocuments

import com.google.gson.annotations.SerializedName
import com.ivan.letstalk.model.documents.Data

data class DeleteDocumentResponse (
    @SerializedName("data") val data : Data,
    @SerializedName("message") val message : String,
    @SerializedName("status") val status : String
)