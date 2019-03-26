package com.pechuro.cashdebts.ui.fragment.profileedit.model

import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.data.model.FirestoreUser

class ProfileEditModel {
    val fields = ProfileEditFields()
    val errors = ProfileEditFieldsError()

    fun setUser(user: FirestoreUser) {
        with(fields) {
            firstName = user.firstName
            lastName = user.lastName
            imageUrl = user.photoUrl
        }
    }

    fun isValid(): Boolean {
        val isValid = isFirstNameValid() && isLastNameValid()
        return isValid
    }

    private fun isFirstNameValid(): Boolean =
        if (fields.firstName.matches(NAME_REGEX)) {
            errors.firstNameError = null
            true
        } else {
            errors.firstNameError = R.string.profile_edit_error_first_name
            false
        }

    private fun isLastNameValid(): Boolean =
        if (fields.lastName.matches(NAME_REGEX)) {
            errors.lastNameError = null
            true
        } else {
            errors.lastNameError = R.string.profile_edit_error_last_name
            false
        }

    companion object {
        private val NAME_REGEX = """^\p{Lu}\p{Ll}*(?:-\p{Lu}\p{Ll}*)?${'$'}""".toRegex()
    }
}