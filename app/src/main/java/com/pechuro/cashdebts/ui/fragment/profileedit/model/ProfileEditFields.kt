package com.pechuro.cashdebts.ui.fragment.profileedit.model

data class ProfileEditFields(
    var imageUrl: String?,
    var firstName: String,
    var lastName: String
) {
    constructor() : this(null, "", "")
}