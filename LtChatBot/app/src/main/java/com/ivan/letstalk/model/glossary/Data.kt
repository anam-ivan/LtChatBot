package com.ivan.letstalk.model.glossary

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("_id") var Id: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("short_description") var shortDescription: String? = null,
    @SerializedName("slug") var slug: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("symptoms") var symptoms: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null
) : Comparable<Any> {
    override fun compareTo(other: Any): Int {
        TODO("Not yet implemented")
    }
}