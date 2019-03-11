package com.pechuro.cashdebts.data

import com.google.firebase.auth.FirebaseAuth

class CurrentUser(private val auth: FirebaseAuth) {
    val phoneNumber: String?
        get() = auth.currentUser?.phoneNumber
    val name: String?
        get() = auth.currentUser?.displayName
}