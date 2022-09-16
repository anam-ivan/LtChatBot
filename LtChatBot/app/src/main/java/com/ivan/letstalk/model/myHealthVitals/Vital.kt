package com.ivan.letstalk.model.myHealthVitals

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/*
data class Vital(
  @SerializedName("_id") var Id: String? = null,
  @SerializedName("create_date") var createDate: String? = null,
  @SerializedName("date") var date: String? = null,
  @SerializedName("icon") var icon: String? = null,
  @SerializedName("meta_id") var metaId: String? = null,
  @SerializedName("status") var status: String? = null,
  @SerializedName("time") var time: String? = null,
  @SerializedName("title") var title: String? = null,
  @SerializedName("title_datetime") var titleDatetime: String? = null,
  @SerializedName("unit") var unit: String? = null,
  @SerializedName("update_date") var updateDate: String? = null,
  @SerializedName("user_id") var userId: String? = null,
  @SerializedName("value") var value: String? = null
) :Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString()
  ) {
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(Id)
    parcel.writeString(createDate)
    parcel.writeString(date)
    parcel.writeString(icon)
    parcel.writeString(metaId)
    parcel.writeString(status)
    parcel.writeString(time)
    parcel.writeString(title)
    parcel.writeString(titleDatetime)
    parcel.writeString(unit)
    parcel.writeString(updateDate)
    parcel.writeString(userId)
    parcel.writeString(value)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<Vital> {
    override fun createFromParcel(parcel: Parcel): Vital {
      return Vital(parcel)
    }

    override fun newArray(size: Int): Array<Vital?> {
      return arrayOfNulls(size)
    }
  }
}*/
data class Vital(
  @SerializedName("_id") var Id: String? = null,
  @SerializedName("create_date") var createDate: String? = null,
  @SerializedName("date") var date: String? = null,
  @SerializedName("icon") var icon: String? = null,
  @SerializedName("meta_id") var metaId: String? = null,
  @SerializedName("status") var status: String? = null,
  @SerializedName("time") var time: String? = null,
  @SerializedName("title") var title: String? = null,
  @SerializedName("title_datetime") var titleDatetime: String? = null,
  @SerializedName("unit") var unit: String? = null,
  @SerializedName("update_date") var updateDate: String? = null,
  @SerializedName("user_id") var userId: String? = null,
  @SerializedName("value") var value: String? = null
) : Serializable
