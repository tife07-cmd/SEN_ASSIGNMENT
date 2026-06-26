package com.tife.tifescienticcalculator.core.engine

class Tokenizer {
    private val functions = setOf(
        "sin", "cos", "tan", "asin", "acos", "atan",
        "sinh", "cosh", "tanh", "asinh", "acosh", "atanh",
        "ln", "log", "sqrt", "cbrt", "exp", "abs"
    )
    private val constants = setOf("pi", "e")

    fun tokenize(input: String): List<Token> {
        val s = input.replace(" ", "")
        val tokens = mutableListOf<Token>()
        var i = 0
        while (i < s.length) {
            val c = s[i]
            when {
                c.isDigit() || c == '.' -> {
                    val start = i
                    while (i < s.length && (s[i].isDigit() || s[i] == '.')) i++
                    tokens += Token(TokenType.NUMBER, s.substring(start, i))
                }
                // uppercase P and C are the nPr / nCr binary operators; check before the
                // letter branch since they would otherwise be read as function names
                c == 'P' || c == 'C' -> { tokens += Token(TokenType.OPERATOR, c.toString()); i++ }
                c.isLetter() -> {
                    val start = i
                    while (i < s.length && s[i].isLetter()) i++
                    val name = s.substring(start, i).lowercase()
                    when (name) {
                        in functions -> tokens += Token(TokenType.FUNCTION, name)
                        in constants -> tokens += Token(TokenType.CONSTANT, name)
                        else -> throw CalcException("Unknown name: $name")
                    }
                }
                c == '(' -> { tokens += Token(TokenType.LPAREN, "("); i++ }
                c == ')' -> { tokens += Token(TokenType.RPAREN, ")"); i++ }
                c == '!' -> { tokens += Token(TokenType.FACTORIAL, "!"); i++ }
                c in "+-*/^" -> {
                    val prev = tokens.lastOrNull()
                    val unaryContext = prev == null ||
                        prev.type == TokenType.OPERATOR ||
                        prev.type == TokenType.UNARY ||
                        prev.type == TokenType.LPAREN
                    when {
                        c == '-' && unaryContext -> tokens += Token(TokenType.UNARY, "u")
                        c == '+' && unaryContext -> { /* unary plus is a no-op */ }
                        else -> tokens += Token(TokenType.OPERATOR, c.toString())
                    }
                    i++
                }
                else -> throw CalcException("Unexpected character: $c")
            }
        }
        return tokens
    }
}
