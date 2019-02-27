package com.pechuro.cashdebts.ui.activity.main

import com.google.firebase.auth.FirebaseAuth
import com.pechuro.cashdebts.ui.base.BaseViewModel
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(private val auth: FirebaseAuth) : BaseViewModel() {

    fun logout() {
        auth.signOut()
    }

}
