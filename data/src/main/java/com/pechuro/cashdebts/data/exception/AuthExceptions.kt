package com.pechuro.cashdebts.data.exception

abstract class AuthException : Exception()

class AuthInvalidCredentialsException : AuthException()
class AuthNotAvailableException : AuthException()
class AuthUnknownException : AuthException()
