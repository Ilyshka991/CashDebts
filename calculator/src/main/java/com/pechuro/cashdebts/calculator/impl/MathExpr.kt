package com.pechuro.cashdebts.calculator.impl

import com.pechuro.cashdebts.calculator.model.Stack
import java.math.BigDecimal

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
        stack.push(stack.pop() + stack.pop())
    }
}

internal object MathMinus : MathExpr() {
    override fun interpret(stack: Stack<BigDecimal>) {
        stack.push(-stack.pop() + stack.pop())
    }
}

internal object MathMultiply : MathExpr() {
    override fun interpret(stack: Stack<BigDecimal>) {
        stack.push(stack.pop() * stack.pop())
    }
}

internal object MathDivide : MathExpr() {
    override fun interpret(stack: Stack<BigDecimal>) {
        val second = stack.pop()
        if (second == BigDecimal.ZERO) throw IllegalArgumentException()
        val first = stack.pop()
        stack.push(first / second)
    }
}