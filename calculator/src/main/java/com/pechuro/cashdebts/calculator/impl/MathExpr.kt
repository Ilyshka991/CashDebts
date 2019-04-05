package com.pechuro.cashdebts.calculator.impl

import com.pechuro.cashdebts.calculator.model.Stack

internal sealed class MathExpr {
    abstract fun interpret(stack: Stack<Double>)
}

internal class MathNumber(private val value: Double) : MathExpr() {
    override fun interpret(stack: Stack<Double>) {
        stack.push(value)
    }
}

internal object MathPlus : MathExpr() {
    override fun interpret(stack: Stack<Double>) {
        stack.push(stack.pop() + stack.pop())
    }
}

internal object MathMinus : MathExpr() {
    override fun interpret(stack: Stack<Double>) {
        stack.push(-stack.pop() + stack.pop())
    }
}

internal object MathMultiply : MathExpr() {
    override fun interpret(stack: Stack<Double>) {
        stack.push(stack.pop() * stack.pop())
    }
}

internal object MathDivide : MathExpr() {
    override fun interpret(stack: Stack<Double>) {
        stack.push(1 / stack.pop() / stack.pop())
    }
}