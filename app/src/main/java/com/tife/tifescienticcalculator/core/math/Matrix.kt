package com.tife.tifescienticcalculator.core.math

import com.tife.tifescienticcalculator.core.engine.CalcException

class Matrix(val rows: Int, val cols: Int, val data: Array<DoubleArray>) {

    init {
        require(data.size == rows && data.all { it.size == cols }) { "Bad matrix shape" }
    }

    operator fun plus(o: Matrix): Matrix { sameShape(o); return build { r, c -> data[r][c] + o.data[r][c] } }
    operator fun minus(o: Matrix): Matrix { sameShape(o); return build { r, c -> data[r][c] - o.data[r][c] } }

    operator fun times(o: Matrix): Matrix {
        if (cols != o.rows) throw CalcException("A columns must equal B rows")
        return Matrix(rows, o.cols, Array(rows) { r ->
            DoubleArray(o.cols) { c ->
                var sum = 0.0
                for (k in 0 until cols) sum += data[r][k] * o.data[k][c]
                sum
            }
        })
    }

    fun scalar(k: Double): Matrix = build { r, c -> data[r][c] * k }

    fun transpose(): Matrix = Matrix(cols, rows, Array(cols) { r -> DoubleArray(rows) { c -> data[c][r] } })

    fun determinant(): Double {
        if (rows != cols) throw CalcException("Determinant needs a square matrix")
        return det(data)
    }

    fun inverse(): Matrix {
        if (rows != cols) throw CalcException("Inverse needs a square matrix")
        val d = determinant()
        if (d == 0.0) throw CalcException("Matrix is singular; no inverse")
        return adjugate().scalar(1.0 / d)
    }

    private fun build(f: (Int, Int) -> Double): Matrix =
        Matrix(rows, cols, Array(rows) { r -> DoubleArray(cols) { c -> f(r, c) } })

    private fun sameShape(o: Matrix) {
        if (rows != o.rows || cols != o.cols) throw CalcException("Matrix sizes do not match")
    }

    private fun det(m: Array<DoubleArray>): Double {
        val n = m.size
        if (n == 1) return m[0][0]
        if (n == 2) return m[0][0] * m[1][1] - m[0][1] * m[1][0]
        var result = 0.0
        for (c in 0 until n) result += (if (c % 2 == 0) 1 else -1) * m[0][c] * det(minor(m, 0, c))
        return result
    }

    private fun minor(m: Array<DoubleArray>, skipRow: Int, skipCol: Int): Array<DoubleArray> {
        val rowsLeft = m.indices.filter { it != skipRow }
        val colsLeft = m[0].indices.filter { it != skipCol }
        return Array(rowsLeft.size) { r -> DoubleArray(colsLeft.size) { c -> m[rowsLeft[r]][colsLeft[c]] } }
    }

    private fun adjugate(): Matrix {
        val cof = Array(rows) { r ->
            DoubleArray(cols) { c ->
                (if ((r + c) % 2 == 0) 1 else -1) * det(minor(data, r, c))
            }
        }
        return Matrix(rows, cols, cof).transpose()
    }

    fun render(): String =
        data.joinToString("\n") { row -> row.joinToString("   ") { v -> "%.3f".format(v) } }
}
