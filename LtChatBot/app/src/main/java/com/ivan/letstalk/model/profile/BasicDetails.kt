package com.ivan.letstalk.model.profile

import com.google.gson.annotations.SerializedName


data class BasicDetails(

  @SerializedName("Date of Diagnosis") var dateOfDiagnosis: String? = null,
  @SerializedName("_id") var Id: String? = null,
  @SerializedName("cr_no") var crNo: String? = null,
  @SerializedName("dob") var dob: String? = null,
  @SerializedName("email_id") var emailId: String? = null,
  @SerializedName("image") var image: String? = null,
  @SerializedName("name") var name: String? = null,
  @SerializedName("phone_number") var phoneNumber: String? = null

)