package com.ivan.letstalk.model.profileUpdate

import com.google.gson.annotations.SerializedName

data class ProfileUpdateResponse(
    @SerializedName("message") var message: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("updated_data") var updatedData: UpdatedData? = UpdatedData()
)