package com.pechuro.cashdebts.data.exception

abstract class FirestoreException(msg: String? = null) : Exception(msg)

class FirestoreCommonException(msg: String? = null) : FirestoreException(msg)
class FirestoreUserNotFoundException : FirestoreException()

