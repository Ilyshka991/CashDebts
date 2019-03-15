package com.pechuro.cashdebts.data.model

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

internal class CurrentUser @Inject constructor(private val auth: FirebaseAuth) {
    val uid: String?
        get() = auth.currentUser?.uid
    val phoneNumber: String?
        get() = auth.currentUser?.phoneNumber
    val isUserSigned: Boolean
        get() = auth.currentUser == null
}