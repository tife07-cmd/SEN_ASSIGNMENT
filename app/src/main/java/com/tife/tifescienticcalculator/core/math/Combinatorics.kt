package com.tife.tifescienticcalculator.core.math

import com.tife.tifescienticcalculator.core.engine.CalcException
import java.math.BigInteger

object Combinatorics {
    fun factorial(n: Int): BigInteger {
        if (n < 0) throw CalcException("n must be non-negative")
        var result = BigInteger.ONE
        for (k in 2..n) result = result.multiply(BigInteger.valueOf(k.toLong()))
        return result
    }

    fun permutations(n: Int, r: Int): BigInteger {
        guard(n, r)
        return factorial(n).divide(factorial(n - r))
    }

    fun combinations(n: Int, r: Int): BigInteger {
        guard(n, r)
        return factorial(n).divide(factorial(r).multiply(factorial(n - r)))
    }

    private fun guard(n: Int, r: Int) {
        if (n < 0 || r < 0) throw CalcException("Values must be non-negative")
        if (r > n) throw CalcException("r must not exceed n")
    }
}
