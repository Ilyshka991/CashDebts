package com.pechuro.cashdebts.data.model

import com.google.firebase.auth.FirebaseAuth

object User {
    val currentUserPhone = FirebaseAuth.getInstance().currentUser!!.phoneNumber!!
}