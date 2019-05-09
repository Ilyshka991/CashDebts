package com.pechuro.cashdebts.ui.fragment.profileedit.model

import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.utils.extensions.requireValue

class ProfileEditModel {
    val fields = ProfileEditFields()
    val errors = ProfileEditFieldsError()

    fun isValid(): Boolean {
        return isFirstNameValid() && isLastNameValid()
    }

    private fun isFirstNameValid(): Boolean =
        if (fields.firstName.requireValue.matches(NAME_REGEX)) {
            errors.firstNameError.onNext(ID_NO_ERROR)
            true
        } else {
            errors.firstNameError.onNext(R.string.fragment_profile_edit_error_first_name)
            false
        }

    private fun isLastNameValid(): Boolean =
        if (fields.lastName.requireValue.matches(NAME_REGEX)) {
            errors.lastNameError.onNext(ID_NO_ERROR)
            true
        } else {
            errors.lastNameError.onNext(R.string.fragment_profile_edit_error_last_name)
            false
        }

    companion object {
        private val NAME_REGEX = """^\p{Lu}\p{Ll}*(?:-\p{Lu}\p{Ll}*)?${'$'}""".toRegex()
        const val ID_NO_ERROR = -1
    }
}