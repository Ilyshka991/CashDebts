package com.pechuro.cashdebts.ui.fragment.profileedit.model

import io.reactivex.subjects.BehaviorSubject

class ProfileEditFieldsError {
    val firstNameError = BehaviorSubject.createDefault(-1)
    val lastNameError = BehaviorSubject.createDefault(-1)
}