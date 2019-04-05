package com.pechuro.cashdebts.calculator.impl

import com.pechuro.cashdebts.calculator.Calculator
import com.pechuro.cashdebts.calculator.model.emptyStackOf
import javax.inject.Inject

internal class CalculatorImpl @Inject constructor() : Calculator {

    override fun evaluate(expr: String): Double {
        val stack = emptyStackOf<Double>()
        val exprInPolishNotation = ""
        val exprList = parse(exprInPolishNotation)
        exprList.forEach {
            it.interpret(stack)
        }
        return stack.pop()
    }

    private fun parse(exprInPolishNotation: String): List<MathExpr> {
        val result = mutableListOf<MathExpr>()
        exprInPolishNotation.split(' ').forEach {
            result.add(
                when (it) {
                    "+" -> MathPlus
                    "-" -> MathMinus
                    "*" -> MathMultiply
                    "/" -> MathDivide
                    else -> MathNumber(it.toDouble())
                }
            )
        }
        return result
    }
}