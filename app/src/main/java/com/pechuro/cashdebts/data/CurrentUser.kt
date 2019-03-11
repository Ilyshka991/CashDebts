package com.pechuro.cashdebts.data

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class CurrentUser(private val auth: FirebaseAuth) {
    val phoneNumber: String?
        get() = auth.currentUser?.phoneNumber
    val displayName: String?
        get() = auth.currentUser?.displayName
    val photoUrl: Uri?
        get() = auth.currentUser?.photoUrl

    fun update(displayName: String? = null, photoUrl: Uri? = null) {
        val request = UserProfileChangeRequest.Builder()
        displayName?.let { request.setDisplayName(displayName) }
        photoUrl?.let { request.setPhotoUri(photoUrl) }
        auth.currentUser?.updateProfile(request.build())
    }
}