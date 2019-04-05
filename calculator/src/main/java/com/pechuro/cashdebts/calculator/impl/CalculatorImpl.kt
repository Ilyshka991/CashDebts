package com.pechuro.cashdebts.calculator.impl

import com.pechuro.cashdebts.calculator.Calculator
import com.pechuro.cashdebts.calculator.Result
import com.pechuro.cashdebts.calculator.Result.Error
import com.pechuro.cashdebts.calculator.model.mutableStackOf
import java.math.BigDecimal
import javax.inject.Inject

internal class CalculatorImpl @Inject constructor(
    private val polishInterpreter: PolishNotationInterpreter
) : Calculator {

    override fun evaluate(expr: String): Result {
        val stack = mutableStackOf<BigDecimal>()

        val exprInPolishNotation = polishInterpreter.interpret(expr) ?: return Error
        val exprList = parse(exprInPolishNotation) ?: return Error

        exprList.forEach {
            try {
                it.interpret(stack)
            } catch (e: IllegalArgumentException) {
                return Error
            }
        }

        return Result.Success(stack.pop())
    }

    private fun parse(exprInPolishNotation: String): List<MathExpr>? {
        val result = mutableListOf<MathExpr>()
        exprInPolishNotation.split(' ').forEach {
            result.add(
                when (it) {
                    "+" -> MathPlus
                    "-" -> MathMinus
                    "*" -> MathMultiply
                    "/" -> MathDivide
                    else -> {
                        val number = try {
                            BigDecimal(it)
                        } catch (e: NumberFormatException) {
                            return null
                        }
                        MathNumber(number)
                    }
                }
            )
        }
        return result
    }
}