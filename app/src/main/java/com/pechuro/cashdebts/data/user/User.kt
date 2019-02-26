package com.pechuro.cashdebts.data.user

import com.google.firebase.auth.FirebaseAuth

object User {
    val currentUserPhone = FirebaseAuth.getInstance().currentUser!!.phoneNumber!!
}