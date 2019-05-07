package com.pechuro.cashdebts.ui.fragment.remotedebtlist.data

import android.os.Parcel
import android.os.Parcelable
import com.pechuro.cashdebts.data.data.model.DebtRole
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus
import java.util.*

data class RemoteDebt(
    val id: String,
    val user: User,
    var value: Double,
    val description: String,
    val date: Date,
    @FirestoreDebtStatus val status: Int,
    @DebtRole var role: Int,
    val isCurrentUserInit: Boolean,
    var isExpanded: Boolean = false,
    val isLocal: Boolean,
    var isUnited: Boolean = false
) {
    fun isEmpty() = value == 0.0

    companion object {
        val EMPTY = RemoteDebt(
            "",
            User(),
            0.0,
            "",
            Date(),
            FirestoreDebtStatus.NOT_SEND,
            DebtRole.CREDITOR,
            isCurrentUserInit = false,
            isExpanded = false,
            isLocal = false,
            isUnited = false
        )
    }

    data class User(
        val uid: String,
        val firstName: String,
        val lastName: String,
        val phoneNumber: String,
        val photoUrl: String?
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()
        )

        constructor() : this("", "", "", "", null)

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(uid)
            parcel.writeString(firstName)
            parcel.writeString(lastName)
            parcel.writeString(phoneNumber)
            parcel.writeString(photoUrl)
        }

        override fun describeContents() = 0

        companion object CREATOR : Parcelable.Creator<User> {
            override fun createFromParcel(parcel: Parcel) = User(parcel)

            override fun newArray(size: Int): Array<User?> {
                return arrayOfNulls(size)
            }
        }
    }
}