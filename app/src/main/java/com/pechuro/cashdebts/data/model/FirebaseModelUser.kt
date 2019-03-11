package com.pechuro.cashdebts.data.model

data class FirestoreUser(
    var firstName: String,
    var lastName: String,
    var phoneNumber: String,
    var photoUrl: String?
) {
    constructor() : this("", "", "", null)
}
