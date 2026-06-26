package com.tife.tifescienticcalculator.core.math

import kotlin.math.*

object Scientific {
    fun factorial(n: Double): Double {
        if (n < 0) return Double.NaN
        if (n == 0.0 || n == 1.0) return 1.0
        var result = 1.0
        for (i in 2..n.toInt()) {
            result *= i
        }
        return result
    }

    fun nCr(n: Int, r: Int): Double {
        if (r < 0 || r > n) return 0.0
        return factorial(n.toDouble()) / (factorial(r.toDouble()) * factorial((n - r).toDouble()))
    }

    fun nPr(n: Int, r: Int): Double {
        if (r < 0 || r > n) return 0.0
        return factorial(n.toDouble()) / factorial((n - r).toDouble())
    }
}
