package com.pechuro.cashdebts.ui.fragment.profileedit.model

import io.reactivex.subjects.BehaviorSubject

class ProfileEditFields {
    val firstName = BehaviorSubject.createDefault("")
    val lastName = BehaviorSubject.createDefault("")
}