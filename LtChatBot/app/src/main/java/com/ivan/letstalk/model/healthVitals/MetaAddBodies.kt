package com.ivan.letstalk.model.healthVitals

object MetaAddBodies {
    data class MetaAddBody(
        val title: String,
        val value: String,
        val meta_id: String,
        val date: String,
        val time: String
    )
}