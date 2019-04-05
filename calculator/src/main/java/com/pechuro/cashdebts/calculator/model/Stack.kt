package com.pechuro.cashdebts.calculator.model

import java.util.*

class Stack<T : Any> {
    private val values = ArrayDeque<T>()

    val isEmpty: Boolean
        get() = values.isEmpty()

    fun push(value: T) {
        values.push(value)
    }

    fun pop() = values.pop()

    fun peek() = values.peek()
}

fun <T : Any> emptyStackOf() = Stack<T>()