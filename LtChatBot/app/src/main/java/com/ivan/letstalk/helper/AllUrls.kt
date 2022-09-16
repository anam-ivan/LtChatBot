package com.ivan.letstalk.helper

object AllUrls {
    var baseUrl: String? = null
    var loginUrl: String? = null
    var commonBaseUrl: String? = null

    init {
        // baseUrl = "http://letstalk.dev13.ivantechnology.in/patient/"
        baseUrl = "http://letstalk.dev13.ivantechnology.in/user/"
        commonBaseUrl = "http://letstalk.dev13.ivantechnology.in/common/"
        loginUrl = "login"
    }
}