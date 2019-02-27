package com.pechuro.cashdebts.data.model

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class CurrentUser @Inject constructor(private val auth: FirebaseAuth) {
    val phoneNumber: String?
        get() {
            return auth.currentUser?.phoneNumber
        }
}