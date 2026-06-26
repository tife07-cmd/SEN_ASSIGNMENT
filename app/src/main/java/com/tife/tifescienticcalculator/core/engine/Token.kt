package com.tife.tifescienticcalculator.core.engine

enum class TokenType { NUMBER, OPERATOR, UNARY, FUNCTION, CONSTANT, LPAREN, RPAREN, FACTORIAL }

data class Token(val type: TokenType, val text: String)
