package com.ivan.letstalk.model.healthVitals

object MetaUpdateBodies {
    data class MetaUpdateBody(
        val _id: String,
        val title: String,
        val value: String,
        val meta_id: String,
        val date: String,
        val time: String
    )
}