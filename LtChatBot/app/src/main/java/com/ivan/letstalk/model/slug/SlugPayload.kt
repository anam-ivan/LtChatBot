package com.ivan.letstalk.model.slug

import com.google.gson.annotations.SerializedName
import com.ivan.letstalk.model.faq.SortBy

object SlugPayload {
    data class Slug(
        @SerializedName("slug") val slug: String,
    )
}