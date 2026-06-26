package com.tife.tifescienticcalculator.core.engine

class ShuntingYard {

    // unary minus binds looser than ^ so -3^2 evaluates to -(3^2)
    private fun precedence(t: Token): Int = when {
        t.text == "P" || t.text == "C" -> 5
        t.text == "^" -> 4
        t.type == TokenType.UNARY -> 3
        t.text == "*" || t.text == "/" -> 2
        t.text == "+" || t.text == "-" -> 1
        else -> 0
    }

    private fun rightAssociative(t: Token): Boolean =
        t.type == TokenType.UNARY || t.text == "^"

    fun toRpn(tokens: List<Token>): List<Token> {
        val output = mutableListOf<Token>()
        val stack = ArrayDeque<Token>()
        for (t in tokens) {
            when (t.type) {
                TokenType.NUMBER, TokenType.CONSTANT -> output += t
                TokenType.FACTORIAL -> output += t
                TokenType.FUNCTION, TokenType.UNARY -> stack.addLast(t)
                TokenType.OPERATOR -> {
                    while (stack.isNotEmpty() && stack.last().type != TokenType.LPAREN &&
                        (precedence(stack.last()) > precedence(t) ||
                            (precedence(stack.last()) == precedence(t) && !rightAssociative(t)))
                    ) output += stack.removeLast()
                    stack.addLast(t)
                }
                TokenType.LPAREN -> stack.addLast(t)
                TokenType.RPAREN -> {
                    while (stack.isNotEmpty() && stack.last().type != TokenType.LPAREN)
                        output += stack.removeLast()
                    if (stack.isEmpty()) throw CalcException("Mismatched parentheses")
                    stack.removeLast()
                    if (stack.isNotEmpty() && stack.last().type == TokenType.FUNCTION)
                        output += stack.removeLast()
                }
            }
        }
        while (stack.isNotEmpty()) {
            val top = stack.removeLast()
            if (top.type == TokenType.LPAREN) throw CalcException("Mismatched parentheses")
            output += top
        }
        return output
    }
}
