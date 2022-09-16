package com.ivan.letstalk.model.login

class VerifyOTPBodies {
    data class OTPLoginBody(
        val user_id: String,
        val otp: String
    )
}