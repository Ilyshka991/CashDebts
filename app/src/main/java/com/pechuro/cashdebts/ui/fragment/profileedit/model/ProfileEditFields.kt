package com.pechuro.cashdebts.ui.fragment.profileedit.model

import com.pechuro.cashdebts.data.model.FirestoreUser
import io.reactivex.subjects.BehaviorSubject

class ProfileEditFields {
    val firstName = BehaviorSubject.createDefault("")
    val lastName = BehaviorSubject.createDefault("")

    fun setUser(user: FirestoreUser) {
        firstName.onNext(user.firstName)
        lastName.onNext(user.lastName)
    }
}