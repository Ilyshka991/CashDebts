package com.pechuro.cashdebts.calculator.impl

import com.pechuro.cashdebts.calculator.model.mutableStackOf
import javax.inject.Inject

internal class PolishNotationInterpreter @Inject constructor() {

    fun interpret(expr: String): String? {
        val exprWithUnaryMinus = replaceUnaryMinus(expr)
        if (!isValid(exprWithUnaryMinus)) return null
        val tokens = getTokens(exprWithUnaryMinus)
        return parse(tokens)
    }

    private fun parse(tokens: List<String>): String? {
        val result = StringBuilder()
        val stack = mutableStackOf<String>()

        tokens.forEach { token ->
            if (token in OPERATORS + PARENTHESES) {
                if (token == "(") {
                    stack.push(token)
                } else {
                    if (token == ")") {
                        while (stack.peek() != "(") {
                            if (stack.isEmpty) return null
                            result.append(stack.pop()).append(" ")
                        }
                        stack.pop()
                    } else {
                        while (stack.isNotEmpty && token.priority() <= stack.peek().priority()) {
                            result.append(stack.pop()).append(" ")
                        }
                        stack.push(token)
                    }
                }
            } else {
                if (token.isNumber()) result.append(token).append(" ") else return null
            }
        }

        while (stack.isNotEmpty) {
            if (stack.peek() == "(") {
                return null
            } else {
                result.append(stack.pop()).append(" ")
            }
        }

        return result.trimEnd().toString()
    }

    private fun getTokens(expr: String): List<String> {
        val exprWithoutSpaces = expr.replace(" ", "")
        val tokens = exprWithoutSpaces.split(INFIX_SPLIT_REGEX)
        return tokens.filter { it.isNotEmpty() }
    }

    private fun replaceUnaryMinus(expr: String): String {
        val result = StringBuilder()
        expr.forEachIndexed { index, char ->
            when {
                char == '-' && index != expr.lastIndex && index != 0
                        && expr[index + 1] == '(' && expr[index - 1] in OPERATORS + PARENTHESES -> result.append("~1*")
                index == 0 && char == '-' -> result.append('~')
                char == '-' && index != 0 && index != expr.lastIndex &&
                        expr[index - 1] in OPERATORS + PARENTHESES && expr[index + 1].isDigit() -> result.append('~')
                else -> result.append(char)
            }
        }
        return result.toString()
    }

    private fun isValid(expr: String): Boolean {
        expr.forEachIndexed { index, char ->
            if ((index == 0 && char in OPERATORS) ||
                (index == expr.lastIndex && char in OPERATORS) ||
                (index != 0 && char in OPERATORS && expr[index - 1] in OPERATORS + PARENTHESES) ||
                (index != expr.lastIndex && char in OPERATORS && expr[index + 1] == ')') ||
                (index != expr.lastIndex && char.isDigit() && expr[index + 1] == '(')
            ) return false
        }
        return true
    }

    private fun String.priority() = when (this) {
        "(" -> 1
        "+", "-" -> 2
        "*", "/" -> 3
        else -> 4
    }

    private fun String.isNumber() = this.matches(NUMBER_REGEX)

    companion object {
        private val INFIX_SPLIT_REGEX = "(?<=[-+*/()])|(?=[-+*/()])".toRegex()
        private val NUMBER_REGEX = "~?\\d+(\\.\\d+)?".toRegex()
        private const val OPERATORS = "+-*/"
        private const val PARENTHESES = "()"
    }
}