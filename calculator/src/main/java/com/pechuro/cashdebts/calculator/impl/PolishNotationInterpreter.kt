package com.pechuro.cashdebts.calculator.impl

import javax.inject.Inject

internal class PolishNotationInterpreter @Inject constructor() {

    fun interpret(expr: String) = parse(expr)

    private fun parse(expr: String): String? {
        val result = StringBuilder()
        return result.toString()
    }
}