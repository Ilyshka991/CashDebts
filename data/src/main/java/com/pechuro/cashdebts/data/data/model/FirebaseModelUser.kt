package com.pechuro.cashdebts.data.data.model

import androidx.annotation.Keep

@Keep
data class FirestoreUser(
    var firstName: String,
    var lastName: String,
    var phoneNumber: String,
    var photoUrl: String?
)
