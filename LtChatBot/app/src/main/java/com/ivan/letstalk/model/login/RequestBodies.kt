package com.ivan.letstalk.model.login

object RequestBodies {
    data class LoginBody(
        val email_id: String,
        val cr_no: String,
        val phone_number: String
    )
}