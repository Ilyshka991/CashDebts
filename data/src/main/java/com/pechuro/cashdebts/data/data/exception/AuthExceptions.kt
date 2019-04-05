package com.pechuro.cashdebts.data.data.exception

abstract class AuthException : Exception()

class AuthInvalidCredentialsException : com.pechuro.cashdebts.data.data.exception.AuthException()
class AuthNotAvailableException : com.pechuro.cashdebts.data.data.exception.AuthException()
class AuthUnknownException : com.pechuro.cashdebts.data.data.exception.AuthException()
