package com.ivan.letstalk.model.phonenumberupdate

object PhoneNumberChangeBodies {
    data class PhoneNumberUpdateBody(
        val cr_no: String,
        val phone_no: String,
        val confirm_phone_no: String
    )
}