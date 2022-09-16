package com.ivan.letstalk.model.healthVitals

object MetaDeleteBodies {
    data class MetaDeleteBody(
        private val _id: MutableList<String> = mutableListOf<String>(),
        // val _id: String,
        val status: String
    )
}