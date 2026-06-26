package com.tife.tifescienticcalculator.core.engine

import com.tife.tifescienticcalculator.core.math.Scientific
import kotlin.math.*

enum class AngleMode { DEGREES, RADIANS }

class Evaluator(private val angleMode: AngleMode = AngleMode.RADIANS) {

    fun evaluate(rpn: List<Token>): Double {
        val stack = ArrayDeque<Double>()
        for (t in rpn) {
            when (t.type) {
                TokenType.NUMBER -> stack.addLast(t.text.toDouble())
                TokenType.CONSTANT -> stack.addLast(if (t.text == "pi") PI else E)
                TokenType.UNARY -> stack.addLast(-pop(stack))
                TokenType.FACTORIAL -> stack.addLast(Scientific.factorial(pop(stack)))
                TokenType.OPERATOR -> {
                    val b = pop(stack); val a = pop(stack)
                    stack.addLast(applyOperator(t.text, a, b))
                }
                TokenType.FUNCTION -> stack.addLast(applyFunction(t.text, pop(stack)))
                else -> throw CalcException("Invalid token in RPN")
            }
        }
        if (stack.size != 1) throw CalcException("Invalid expression")
        return stack.last()
    }

    private fun pop(s: ArrayDeque<Double>): Double =
        if (s.isEmpty()) throw CalcException("Invalid expression") else s.removeLast()

    private fun applyOperator(op: String, a: Double, b: Double): Double = when (op) {
        "+" -> a + b
        "-" -> a - b
        "*" -> a * b
        "/" -> if (b == 0.0) throw CalcException("Division by zero") else a / b
        "^" -> a.pow(b)
        else -> throw CalcException("Unknown operator $op")
    }

    private fun applyFunction(name: String, x: Double): Double {
        val angled = if (angleMode == AngleMode.DEGREES) Math.toRadians(x) else x
        return when (name) {
            "sin" -> sin(angled); "cos" -> cos(angled); "tan" -> tan(angled)
            "asin" -> fromRadians(asin(x)); "acos" -> fromRadians(acos(x)); "atan" -> fromRadians(atan(x))
            "sinh" -> sinh(x); "cosh" -> cosh(x); "tanh" -> tanh(x)
            "asinh" -> asinh(x); "acosh" -> acosh(x); "atanh" -> atanh(x)
            "ln" -> ln(x); "log" -> log10(x)
            "sqrt" -> sqrt(x); "cbrt" -> cbrt(x); "exp" -> exp(x); "abs" -> abs(x)
            else -> throw CalcException("Unknown function $name")
        }
    }

    private fun fromRadians(rad: Double): Double =
        if (angleMode == AngleMode.DEGREES) Math.toDegrees(rad) else rad
}
