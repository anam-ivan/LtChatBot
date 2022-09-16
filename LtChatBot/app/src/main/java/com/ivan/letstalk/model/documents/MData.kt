package com.ivan.letstalk.model.documents

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class MData(
  @SerializedName("_id") var Id: String? = null,
  @SerializedName("created_at") var createdAt: String? = null,
  @SerializedName("document_category_id") var documentCategoryId: String? = null,
  @SerializedName("document_category_name") var documentCategoryName: String? = null,
  @SerializedName("document_name") var documentName: String? = null,
  @SerializedName("files") var files: ArrayList<String> = arrayListOf(),
  @SerializedName("record_date") var recordDate: String? = null,
  @SerializedName("record_time") var recordTime: String? = null,
  @SerializedName("status") var status: String? = null,
  @SerializedName("updated_at") var updatedAt: String? = null,
  @SerializedName("user_id") var userId: String? = null
): Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    TODO("files"),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString()
  ) {
  }

  override fun describeContents(): Int {
    TODO("Not yet implemented")
  }

  override fun writeToParcel(p0: Parcel?, p1: Int) {
    TODO("Not yet implemented")
  }

  companion object CREATOR : Parcelable.Creator<MData> {
    override fun createFromParcel(parcel: Parcel): MData {
      return MData(parcel)
    }

    override fun newArray(size: Int): Array<MData?> {
      return arrayOfNulls(size)
    }
  }
}