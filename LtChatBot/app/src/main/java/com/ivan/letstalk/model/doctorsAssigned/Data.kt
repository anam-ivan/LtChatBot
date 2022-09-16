package com.ivan.letstalk.model.doctorsAssigned

import com.google.gson.annotations.SerializedName


data class Data(
  @SerializedName("_id") var Id: String? = null,
  @SerializedName("created_at") var createdAt: String? = null,
  @SerializedName("doctor_details") var doctorDetails: DoctorDetails? = DoctorDetails(),
  @SerializedName("doctor_id") var doctorId: String? = null,
  @SerializedName("patient_id") var patientId: String? = null,
  @SerializedName("status") var status: String? = null,
  @SerializedName("updated_at") var updatedAt: String? = null
)