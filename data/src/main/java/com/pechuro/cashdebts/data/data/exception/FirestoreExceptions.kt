package com.pechuro.cashdebts.data.data.exception

abstract class FirestoreException(msg: String? = null) : Exception(msg)

class FirestoreCommonException(msg: String? = null) : com.pechuro.cashdebts.data.data.exception.FirestoreException(msg)
class FirestoreUserNotFoundException : com.pechuro.cashdebts.data.data.exception.FirestoreException()

