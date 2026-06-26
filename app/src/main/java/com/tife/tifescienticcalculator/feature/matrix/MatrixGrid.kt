package com.tife.tifescienticcalculator.feature.matrix

import android.text.InputType
import android.widget.EditText
import android.widget.GridLayout
import com.tife.tifescienticcalculator.core.engine.CalcException
import com.tife.tifescienticcalculator.core.math.Matrix

class MatrixGrid(private val grid: GridLayout) {

    private var size = 2
    private var cells: Array<Array<EditText>> = arrayOf()

    fun build(n: Int) {
        size = n
        grid.removeAllViews()
        grid.rowCount = n
        grid.columnCount = n
        cells = Array(n) { r ->
            Array(n) { c ->
                EditText(grid.context).apply {
                    inputType = InputType.TYPE_CLASS_NUMBER or
                        InputType.TYPE_NUMBER_FLAG_DECIMAL or
                        InputType.TYPE_NUMBER_FLAG_SIGNED
                    setText("0")
                    setSelectAllOnFocus(true)
                    gravity = android.view.Gravity.CENTER
                    textSize = 16f
                    val lp = GridLayout.LayoutParams(
                        GridLayout.spec(r, 1f),
                        GridLayout.spec(c, 1f)
                    )
                    lp.width = 0
                    lp.setMargins(6, 6, 6, 6)
                    layoutParams = lp
                    grid.addView(this)
                }
            }
        }
    }

    fun read(): Matrix {
        val data = Array(size) { r ->
            DoubleArray(size) { c ->
                cells[r][c].text.toString().trim().toDoubleOrNull()
                    ?: throw CalcException("Fill every cell with a number")
            }
        }
        return Matrix(size, size, data)
    }
}
