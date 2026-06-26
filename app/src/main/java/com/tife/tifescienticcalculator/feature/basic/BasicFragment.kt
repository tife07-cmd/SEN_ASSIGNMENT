package com.tife.tifescienticcalculator.feature.basic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tife.tifescienticcalculator.R
import com.tife.tifescienticcalculator.feature.common.Keypad

class BasicFragment : Fragment() {

    private lateinit var keypad: Keypad

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_basic, container, false)

        keypad = Keypad(
            view.findViewById<TextView>(R.id.tvInput),
            view.findViewById<TextView>(R.id.tvResult)
        )

        val tokens = mapOf(
            R.id.btn0 to "0", R.id.btn1 to "1", R.id.btn2 to "2", R.id.btn3 to "3",
            R.id.btn4 to "4", R.id.btn5 to "5", R.id.btn6 to "6", R.id.btn7 to "7",
            R.id.btn8 to "8", R.id.btn9 to "9", R.id.btnDot to ".",
            R.id.btnAdd to "+", R.id.btnSub to "-", R.id.btnMul to "*", R.id.btnDiv to "/",
            R.id.btnOpenBrac to "(", R.id.btnCloseBrac to ")"
        )
        for ((id, token) in tokens) {
            view.findViewById<Button>(id).setOnClickListener { keypad.append(token) }
        }

        view.findViewById<Button>(R.id.btnEqual).setOnClickListener { keypad.evaluate() }
        view.findViewById<Button>(R.id.btnAC).setOnClickListener { keypad.clear() }
        view.findViewById<Button>(R.id.btnDel).setOnClickListener { keypad.backspace() }

        savedInstanceState?.getString(KEY_EXPR)?.let { keypad.restore(it) }
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::keypad.isInitialized) outState.putString(KEY_EXPR, keypad.expr)
    }

    companion object {
        private const val KEY_EXPR = "basic_expr"
    }
}
