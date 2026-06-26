package com.tife.tifescienticcalculator.feature.combinatorics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tife.tifescienticcalculator.R
import com.tife.tifescienticcalculator.core.engine.CalcException
import com.tife.tifescienticcalculator.core.math.Combinatorics

class CombinatoricsFragment : Fragment() {

    private lateinit var inputN: EditText
    private lateinit var inputR: EditText
    private lateinit var output: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_combinatorics, container, false)

        inputN = view.findViewById(R.id.inputN)
        inputR = view.findViewById(R.id.inputR)
        output = view.findViewById(R.id.tvCombOutput)

        view.findViewById<Button>(R.id.btnNpr).setOnClickListener {
            withNR { n, r -> Combinatorics.permutations(n, r).toString() }
        }
        view.findViewById<Button>(R.id.btnNcr).setOnClickListener {
            withNR { n, r -> Combinatorics.combinations(n, r).toString() }
        }
        view.findViewById<Button>(R.id.btnFactorial).setOnClickListener {
            val n = inputN.text.toString().trim().toIntOrNull()
            output.text = if (n == null) getString(R.string.error_msg)
            else try { Combinatorics.factorial(n).toString() }
            catch (e: CalcException) { e.message }
        }

        return view
    }

    private fun withNR(op: (Int, Int) -> String) {
        val n = inputN.text.toString().trim().toIntOrNull()
        val r = inputR.text.toString().trim().toIntOrNull()
        output.text = if (n == null || r == null) "Enter integers n and r"
        else try { op(n, r) } catch (e: CalcException) { e.message }
    }
}
