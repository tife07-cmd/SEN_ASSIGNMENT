package com.tife.tifescienticcalculator.feature.matrix

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tife.tifescienticcalculator.R
import com.tife.tifescienticcalculator.core.engine.CalcException
import com.tife.tifescienticcalculator.core.format.NumberFormatter
import com.tife.tifescienticcalculator.core.math.Matrix

class MatrixFragment : Fragment() {

    private lateinit var gridA: MatrixGrid
    private lateinit var gridB: MatrixGrid
    private lateinit var output: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_matrix, container, false)

        gridA = MatrixGrid(view.findViewById<GridLayout>(R.id.gridA))
        gridB = MatrixGrid(view.findViewById<GridLayout>(R.id.gridB))
        output = view.findViewById(R.id.tvMatrixOutput)

        val size = savedInstanceState?.getInt(KEY_SIZE, 2) ?: 2
        resize(size)

        view.findViewById<Button>(R.id.btnSize2).setOnClickListener { resize(2) }
        view.findViewById<Button>(R.id.btnSize3).setOnClickListener { resize(3) }
        view.findViewById<Button>(R.id.btnSize4).setOnClickListener { resize(4) }

        view.findViewById<Button>(R.id.btnMatAdd).setOnClickListener { binary { a, b -> (a + b).render() } }
        view.findViewById<Button>(R.id.btnMatSub).setOnClickListener { binary { a, b -> (a - b).render() } }
        view.findViewById<Button>(R.id.btnMatMul).setOnClickListener { binary { a, b -> (a * b).render() } }
        view.findViewById<Button>(R.id.btnMatDet).setOnClickListener { unary { a -> NumberFormatter.format(a.determinant()) } }
        view.findViewById<Button>(R.id.btnMatInv).setOnClickListener { unary { a -> a.inverse().render() } }
        view.findViewById<Button>(R.id.btnMatTrans).setOnClickListener { unary { a -> a.transpose().render() } }

        return view
    }

    private var currentSize = 2

    private fun resize(n: Int) {
        currentSize = n
        gridA.build(n)
        gridB.build(n)
        output.text = getString(R.string.result_placeholder)
    }

    private fun unary(op: (Matrix) -> String) {
        output.text = try {
            op(gridA.read())
        } catch (e: CalcException) {
            e.message ?: getString(R.string.error_msg)
        }
    }

    private fun binary(op: (Matrix, Matrix) -> String) {
        output.text = try {
            op(gridA.read(), gridB.read())
        } catch (e: CalcException) {
            e.message ?: getString(R.string.error_msg)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SIZE, currentSize)
    }

    companion object {
        private const val KEY_SIZE = "matrix_size"
    }
}
