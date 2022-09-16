package com.ivan.letstalk.model.login
import com.google.gson.annotations.SerializedName


data class Data(
    @SerializedName("_id") var Id: String? = null,
    @SerializedName("cr_no") var crNo: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("dob") var dob: String? = null,
    @SerializedName("email_id") var emailId: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("otp") var otp: String? = null,
    @SerializedName("password") var password: String? = null,
    @SerializedName("phone_number") var phoneNumber: String? = null,
    @SerializedName("role_id") var roleId: String? = null,
    @SerializedName("role_name") var roleName: String? = null,
    @SerializedName("sex") var sex: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("token") var token: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null

)