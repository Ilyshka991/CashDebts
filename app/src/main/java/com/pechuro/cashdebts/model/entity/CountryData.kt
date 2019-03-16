package com.pechuro.cashdebts.model.entity

import android.os.Parcel
import android.os.Parcelable

data class CountryData(
    val code: String,
    val phonePrefix: String,
    val name: String,
    val phonePattern: String?
) : Parcelable {
    val isEmpty = code.isEmpty()

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(code)
        parcel.writeString(phonePrefix)
        parcel.writeString(name)
        parcel.writeString(phonePattern)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<CountryData> {
        val EMPTY = CountryData("", "", "", null)

        override fun createFromParcel(parcel: Parcel) = CountryData(parcel)

        override fun newArray(size: Int): Array<CountryData?> = arrayOfNulls(size)
    }
}
