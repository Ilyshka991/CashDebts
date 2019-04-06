package com.pechuro.cashdebts.calculator

interface Calculator {

    fun evaluate(expr: String): Result
}

sealed class Result {
    class Success(val result: Double) : Result()
    object Error : Result()
}