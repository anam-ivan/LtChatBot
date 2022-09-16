package com.ivan.letstalk.model.myHealthVitals

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/*
data class Value(
  @SerializedName("time") var time: String? = null,
  @SerializedName("vital") var vital: ArrayList<Vital> = arrayListOf()
) : Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readString(),
  )

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(time)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<Data> {
    override fun createFromParcel(parcel: Parcel): Data {
      return Data(parcel)
    }

    override fun newArray(size: Int): Array<Data?> {
      return arrayOfNulls(size)
    }
  }
}*/
data class Value(
  @SerializedName("time") var time: String? = null,
  @SerializedName("vital") var vital: ArrayList<Vital> = arrayListOf()
) : Serializable
