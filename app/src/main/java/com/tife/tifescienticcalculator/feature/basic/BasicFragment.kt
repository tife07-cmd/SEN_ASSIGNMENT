package com.tife.tifescienticcalculator.feature.basic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tife.tifescienticcalculator.R

class BasicFragment : Fragment() {

    private lateinit var tvInput: TextView
    private lateinit var tvResult: TextView
    private var input: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_basic, container, false)

        tvInput = view.findViewById(R.id.tvInput)
        tvResult = view.findViewById(R.id.tvResult)

        val buttons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
            R.id.btnDot, R.id.btnAdd, R.id.btnSub, R.id.btnMul, R.id.btnDiv,
            R.id.btnOpenBrac
        )

        for (id in buttons) {
            view.findViewById<Button>(id).setOnClickListener {
                val button = it as Button
                input += button.text.toString()
                tvInput.text = input
            }
        }

        view.findViewById<Button>(R.id.btnAC).setOnClickListener {
            input = ""
            tvInput.text = getString(R.string.btn_0)
            tvResult.text = ""
        }

        view.findViewById<Button>(R.id.btnDel).setOnClickListener {
            if (input.isNotEmpty()) {
                input = input.substring(0, input.length - 1)
                tvInput.text = input.ifEmpty { getString(R.string.btn_0) }
            }
        }

        view.findViewById<Button>(R.id.btnEqual).setOnClickListener {
            tvResult.text = input // Simplified for now
        }

        return view
    }
}
