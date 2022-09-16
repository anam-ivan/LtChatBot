package com.ivan.letstalk.model.profileUpdate

import com.google.gson.annotations.SerializedName


data class UpdatedData(
  @SerializedName("_id") var Id: String? = null,
  @SerializedName("address") var address: String? = null,
  @SerializedName("city") var city: String? = null,
  @SerializedName("cr_no") var crNo: String? = null,
  @SerializedName("created_at") var createdAt: String? = null,
  @SerializedName("dob") var dob: String? = null,
  @SerializedName("email_id") var emailId: String? = null,
  @SerializedName("height") var height: String? = null,
  @SerializedName("image") var image: String? = null,
  @SerializedName("name") var name: String? = null,
  @SerializedName("next_of_kin_mobile") var nextOfKinMobile: String? = null,
  @SerializedName("next_of_kin_name") var nextOfKinName: String? = null,
  @SerializedName("next_of_kin_relation") var nextOfKinRelation: String? = null,
  @SerializedName("password") var password: String? = null,
  @SerializedName("phone_number") var phoneNumber: String? = null,
  @SerializedName("pincode") var pincode: String? = null,
  @SerializedName("role_id") var roleId: String? = null,
  @SerializedName("role_name") var roleName: String? = null,
  @SerializedName("sex") var sex: String? = null,
  @SerializedName("state") var state: String? = null,
  @SerializedName("status") var status: String? = null,
  @SerializedName("updated_at") var updatedAt: String? = null,
  @SerializedName("weight") var weight: String? = null
)