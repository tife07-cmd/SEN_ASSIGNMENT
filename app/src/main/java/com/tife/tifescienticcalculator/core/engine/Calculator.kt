package com.tife.tifescienticcalculator.core.engine

class Calculator(private val angleMode: AngleMode = AngleMode.RADIANS) {
    private val tokenizer = Tokenizer()
    private val shuntingYard = ShuntingYard()

    fun evaluate(expression: String): Double {
        val rpn = shuntingYard.toRpn(tokenizer.tokenize(expression))
        return Evaluator(angleMode).evaluate(rpn)
    }
}
