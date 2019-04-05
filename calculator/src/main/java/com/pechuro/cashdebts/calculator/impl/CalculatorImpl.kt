package com.pechuro.cashdebts.calculator.impl

import com.pechuro.cashdebts.calculator.Calculator
import javax.inject.Inject

internal class CalculatorImpl @Inject constructor() : Calculator {

    override fun evaluate(expr: String): Double {
        return 1.0

    }
}