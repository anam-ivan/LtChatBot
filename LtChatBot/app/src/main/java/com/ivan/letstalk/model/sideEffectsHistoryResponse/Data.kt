package com.ivan.letstalk.model.sideEffectsHistoryResponse

import com.example.example.Med
import com.google.gson.annotations.SerializedName


data class Data(
  @SerializedName("_id") var _id: String? = null,
  @SerializedName("created_at") var created_at: String? = null,
  @SerializedName("med") var med: ArrayList<Med> = arrayListOf(),
  @SerializedName("side_effect_id") var sideEffectId: String? = null,
  @SerializedName("side_effect_name") var sideEffectName: String? = null,
  @SerializedName("status") var status: String? = null,
  @SerializedName("synonyms") var synonyms: ArrayList<Synonyms> = arrayListOf(),
  @SerializedName("updated_at") var updated_at: String? = null,
  @SerializedName("user_id") var userId: String? = null
)