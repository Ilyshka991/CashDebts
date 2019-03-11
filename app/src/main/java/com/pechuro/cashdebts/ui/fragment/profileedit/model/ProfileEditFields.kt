package com.pechuro.cashdebts.ui.fragment.profileedit.model

import android.net.Uri

data class ProfileEditFields(
    var imageUrl: Uri?,
    var firstName: String,
    var lastName: String
) {
    constructor() : this(null, "", "")
}