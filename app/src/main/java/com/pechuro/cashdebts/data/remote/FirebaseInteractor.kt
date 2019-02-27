package com.pechuro.cashdebts.data.remote

import com.google.firebase.auth.FirebaseAuth

class FirebaseInteractor(private val repository: FirestoreRepository, private val auth: FirebaseAuth) {

    fun init() {
        auth.addAuthStateListener {
            if (it.currentUser != null) {
                repository.startSync()
            } else {
                repository.stopSync()
            }
        }
    }
}