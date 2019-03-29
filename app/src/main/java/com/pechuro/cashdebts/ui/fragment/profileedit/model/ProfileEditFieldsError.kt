package com.pechuro.cashdebts.ui.fragment.profileedit.model

import com.pechuro.cashdebts.ui.fragment.profileedit.model.ProfileEditModel.Companion.ID_NO_ERROR
import io.reactivex.subjects.BehaviorSubject

class ProfileEditFieldsError {
    val firstNameError = BehaviorSubject.createDefault(ID_NO_ERROR)
    val lastNameError = BehaviorSubject.createDefault(ID_NO_ERROR)
}