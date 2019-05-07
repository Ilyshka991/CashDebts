package com.pechuro.cashdebts.data.data.model

import android.os.Parcel
import android.os.Parcelable

data class UserBaseInformation(
    val uid: String,
    val phoneNumber: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(phoneNumber)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<UserBaseInformation> {
        override fun createFromParcel(parcel: Parcel) = UserBaseInformation(parcel)

        override fun newArray(size: Int): Array<UserBaseInformation?> {
            return arrayOfNulls(size)
        }
    }
}