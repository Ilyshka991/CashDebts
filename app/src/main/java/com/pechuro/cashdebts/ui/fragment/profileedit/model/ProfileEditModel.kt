package com.pechuro.cashdebts.ui.fragment.profileedit.model

import android.net.Uri
import androidx.databinding.BaseObservable
import com.pechuro.cashdebts.R

class ProfileEditModel() : BaseObservable() {
    val fields = ProfileEditFields()
    val errors = ProfileEditFieldsError()

    constructor(displayName: String?, imageUri: Uri?) : this() {
        displayName?.let {
            val nameList = displayName.split(" ")
            if (nameList.size == 2) {
                fields.apply {
                    firstName = nameList[0]
                    lastName = nameList[1]
                }
            }
        }
        imageUri?.let {
            fields.imageUrl = it
        }
    }

    fun getDisplayName() = "${fields.firstName} ${fields.lastName}"

    fun isValid(): Boolean {
        val isValid = isFirstNameValid() && isLastNameValid()
        notifyChange()
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