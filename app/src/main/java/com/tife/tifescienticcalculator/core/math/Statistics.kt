package com.tife.tifescienticcalculator.core.math

import com.tife.tifescienticcalculator.core.engine.CalcException
import kotlin.math.sqrt

object Statistics {
    fun mean(x: List<Double>): Double { requireData(x); return x.sum() / x.size }

    fun median(x: List<Double>): Double {
        requireData(x)
        val s = x.sorted(); val n = s.size
        return if (n % 2 == 1) s[n / 2] else (s[n / 2 - 1] + s[n / 2]) / 2.0
    }

    fun mode(x: List<Double>): List<Double> {
        requireData(x)
        val freq = x.groupingBy { it }.eachCount()
        val max = freq.values.max()
        return freq.filterValues { it == max }.keys.sorted()
    }

    fun range(x: List<Double>): Double { requireData(x); return x.max() - x.min() }

    fun variance(x: List<Double>, sample: Boolean): Double {
        requireData(x)
        if (sample && x.size < 2) throw CalcException("Sample variance needs at least 2 values")
        val m = mean(x)
        val ss = x.sumOf { (it - m) * (it - m) }
        return ss / (if (sample) x.size - 1 else x.size)
    }

    fun stdDev(x: List<Double>, sample: Boolean): Double = sqrt(variance(x, sample))

    private fun requireData(x: List<Double>) { if (x.isEmpty()) throw CalcException("No data") }
}
