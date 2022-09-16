package com.ivan.letstalk.model.profileUpdate

import com.google.gson.annotations.SerializedName

object ProfileEditPayload {
    data class ProfileEdit(
        @SerializedName("height"  ) var height  : String? = null,
        @SerializedName("weight"  ) var weight  : String? = null,
        @SerializedName("address" ) var address : String? = null,
        @SerializedName("name"    ) var name    : String? = null,
        @SerializedName("city"    ) var city    : String? = null,
        @SerializedName("state"   ) var state   : String? = null,
        @SerializedName("pincode" ) var pincode : String? = null
    )
}