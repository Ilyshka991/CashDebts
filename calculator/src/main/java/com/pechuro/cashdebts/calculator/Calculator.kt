package com.pechuro.cashdebts.calculator

import java.math.BigDecimal

interface Calculator {

    fun evaluate(expr: String): Result
}

sealed class Result {
    class Success(val result: BigDecimal) : Result()
    object Error : Result()
}