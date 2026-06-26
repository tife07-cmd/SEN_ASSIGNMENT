package com.tife.tifescienticcalculator.core.math

import com.tife.tifescienticcalculator.core.engine.CalcException
import kotlin.math.floor

object Scientific {
    fun factorial(n: Double): Double {
        if (n < 0 || n != floor(n)) throw CalcException("Factorial needs a non-negative integer")
        var result = 1.0
        for (k in 2..n.toInt()) result *= k
        return result
    }
}
