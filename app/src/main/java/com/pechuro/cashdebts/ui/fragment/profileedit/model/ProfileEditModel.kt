package com.pechuro.cashdebts.ui.fragment.profileedit.model

import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.data.model.FirestoreUser

class ProfileEditModel {
    val fields = ProfileEditFields()
    val errors = ProfileEditFieldsError()

    fun setUser(user: FirestoreUser) {
        fields.setUser(user)
    }

    fun isValid(): Boolean {
        return isFirstNameValid() && isLastNameValid()
    }

    private fun isFirstNameValid(): Boolean =
        if (fields.firstName.value!!.matches(NAME_REGEX)) {
            errors.firstNameError.onNext(-1)
            true
        } else {
            errors.firstNameError.onNext(R.string.profile_edit_error_first_name)
            false
        }

    private fun isLastNameValid(): Boolean =
        if (fields.lastName.value!!.matches(NAME_REGEX)) {
            errors.lastNameError.onNext(-1)
            true
        } else {
            errors.lastNameError.onNext(R.string.profile_edit_error_last_name)
            false
        }

    companion object {
        private val NAME_REGEX = """^\p{Lu}\p{Ll}*(?:-\p{Lu}\p{Ll}*)?${'$'}""".toRegex()
    }
}