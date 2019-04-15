package com.pechuro.cashdebts.calculator.impl

import com.pechuro.cashdebts.calculator.model.Stack
import java.math.BigDecimal
import java.math.MathContext

internal sealed class MathExpr {
    abstract fun interpret(stack: Stack<BigDecimal>)
}

internal class MathNumber(private val value: BigDecimal) : MathExpr() {
    override fun interpret(stack: Stack<BigDecimal>) {
        stack.push(value)
    }
}

internal object MathPlus : MathExpr() {
    override fun interpret(stack: Stack<BigDecimal>) {
        val second = stack.popIfExist() ?: BigDecimal.ZERO
        val first = stack.popIfExist() ?: BigDecimal.ZERO
        stack.push(first.add(second, MathContext.DECIMAL128))
    }
}

internal object MathMinus : MathExpr() {
    override fun interpret(stack: Stack<BigDecimal>) {
        val second = stack.popIfExist() ?: BigDecimal.ZERO
        val first = stack.popIfExist() ?: BigDecimal.ZERO
        stack.push(first.subtract(second, MathContext.DECIMAL128))
    }
}

internal object MathMultiply : MathExpr() {
    override fun interpret(stack: Stack<BigDecimal>) {
        val second = stack.popIfExist() ?: BigDecimal.ONE
        val first = stack.popIfExist() ?: BigDecimal.ONE
        stack.push(second.multiply(first, MathContext.DECIMAL128))
    }
}

internal object MathDivide : MathExpr() {
    override fun interpret(stack: Stack<BigDecimal>) {
        val second = stack.popIfExist() ?: BigDecimal.ONE
        if (second == BigDecimal.ZERO) throw IllegalArgumentException()
        val first = stack.popIfExist() ?: BigDecimal.ONE
        stack.push(first.divide(second, MathContext.DECIMAL128))
    }
}