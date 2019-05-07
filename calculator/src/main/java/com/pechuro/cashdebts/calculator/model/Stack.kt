package com.pechuro.cashdebts.calculator.model

import java.util.*

class Stack<T : Any> {
    private val values = ArrayDeque<T>()

    val isEmpty: Boolean
        get() = values.isEmpty()
    val isNotEmpty: Boolean
        get() = !isEmpty

    fun push(value: T) {
        values.push(value)
    }

    fun pop(): T = values.pop()

    fun popIfExist() = if (isNotEmpty) pop() else null

    fun peek(): T = values.peek()
}

fun <T : Any> mutableStackOf() = Stack<T>()