package com.tife.tifescienticcalculator

import com.tife.tifescienticcalculator.core.engine.AngleMode
import com.tife.tifescienticcalculator.core.engine.Calculator
import org.junit.Assert.assertEquals
import org.junit.Test

class EvaluatorTest {
    private val calc = Calculator()

    @Test fun respectsPrecedence() = assertEquals(7.0, calc.evaluate("1+2*3"), 1e-9)
    @Test fun handlesParentheses() = assertEquals(9.0, calc.evaluate("(1+2)*3"), 1e-9)
    @Test fun unaryMinusBelowPower() = assertEquals(-9.0, calc.evaluate("-3^2"), 1e-9)
    @Test fun powerIsRightAssociative() = assertEquals(512.0, calc.evaluate("2^3^2"), 1e-9)
    @Test fun factorialOperator() = assertEquals(120.0, calc.evaluate("5!"), 1e-9)
    @Test fun constantsAndFunctions() = assertEquals(0.0, calc.evaluate("sin(0)"), 1e-9)

    @Test fun degreesModeUsesDegrees() {
        val deg = Calculator(AngleMode.DEGREES)
        assertEquals(1.0, deg.evaluate("sin(90)"), 1e-9)
    }

    @Test fun permutationOperator() = assertEquals(20.0, calc.evaluate("5P2"), 1e-9)
    @Test fun combinationOperator() = assertEquals(10.0, calc.evaluate("5C2"), 1e-9)
    @Test fun permutationBindsTighterThanPlus() = assertEquals(22.0, calc.evaluate("2+5P2"), 1e-9)
}
