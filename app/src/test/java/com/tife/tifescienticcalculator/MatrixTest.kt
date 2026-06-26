package com.tife.tifescienticcalculator

import com.tife.tifescienticcalculator.core.math.Combinatorics
import com.tife.tifescienticcalculator.core.math.Matrix
import com.tife.tifescienticcalculator.core.math.Statistics
import org.junit.Assert.assertEquals
import org.junit.Test

class MatrixTest {
    @Test fun determinantOfTwoByTwo() {
        val m = Matrix(2, 2, arrayOf(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0)))
        assertEquals(-2.0, m.determinant(), 1e-9)
    }

    @Test fun inverseTimesOriginalIsIdentity() {
        val m = Matrix(2, 2, arrayOf(doubleArrayOf(4.0, 7.0), doubleArrayOf(2.0, 6.0)))
        val product = m * m.inverse()
        assertEquals(1.0, product.data[0][0], 1e-9)
        assertEquals(0.0, product.data[0][1], 1e-9)
        assertEquals(1.0, product.data[1][1], 1e-9)
    }

    @Test fun permutationsAndCombinations() {
        assertEquals("20", Combinatorics.permutations(5, 2).toString())
        assertEquals("10", Combinatorics.combinations(5, 2).toString())
    }

    @Test fun statisticsMeanAndMedian() {
        val data = listOf(1.0, 2.0, 3.0, 4.0)
        assertEquals(2.5, Statistics.mean(data), 1e-9)
        assertEquals(2.5, Statistics.median(data), 1e-9)
    }
}
