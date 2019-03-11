package com.pechuro.cashdebts.ui.fragment.profileedit.model

data class ProfileEditFieldsError(
    var firstNameError: Int?,
    var lastNameError: Int?
) {
    constructor() : this(null, null)
}