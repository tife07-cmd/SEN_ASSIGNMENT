package com.tife.tifescienticcalculator.core.format

import kotlin.math.abs

object NumberFormatter {
    fun format(value: Double): String {
        if (value.isNaN() || value.isInfinite()) return "Error"
        if (value == 0.0) return "0"
        val a = abs(value)
        return if (a >= 1e10 || a < 1e-6) {
            "%.6e".format(value)
        } else {
            "%.10f".format(value).trimEnd('0').trimEnd('.')
        }
    }
}
