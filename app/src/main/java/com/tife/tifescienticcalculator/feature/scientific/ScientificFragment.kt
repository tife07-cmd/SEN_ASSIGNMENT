package com.tife.tifescienticcalculator.feature.scientific

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tife.tifescienticcalculator.R
import com.tife.tifescienticcalculator.core.engine.AngleMode
import com.tife.tifescienticcalculator.feature.common.Keypad

class ScientificFragment : Fragment() {

    private lateinit var keypad: Keypad
    private lateinit var btnDeg: Button
    private var degrees = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_scientific, container, false)

        keypad = Keypad(
            view.findViewById<TextView>(R.id.tvInput),
            view.findViewById<TextView>(R.id.tvResult)
        )

        val tokens = mapOf(
            R.id.btn0 to "0", R.id.btn1 to "1", R.id.btn2 to "2", R.id.btn3 to "3",
            R.id.btn4 to "4", R.id.btn5 to "5", R.id.btn6 to "6", R.id.btn7 to "7",
            R.id.btn8 to "8", R.id.btn9 to "9", R.id.btnDot to ".",
            R.id.btnAdd to "+", R.id.btnSub to "-", R.id.btnMul to "*", R.id.btnDiv to "/",
            R.id.btnOpenBrac to "(", R.id.btnCloseBrac to ")",
            R.id.btnPow to "^", R.id.btnFact to "!",
            R.id.btnSin to "sin(", R.id.btnCos to "cos(", R.id.btnTan to "tan(",
            R.id.btnAsin to "asin(", R.id.btnAcos to "acos(", R.id.btnAtan to "atan(",
            R.id.btnSinh to "sinh(", R.id.btnCosh to "cosh(", R.id.btnTanh to "tanh(",
            R.id.btnLn to "ln(", R.id.btnLog to "log(", R.id.btnSqrt to "sqrt(",
            R.id.btnExp to "exp(", R.id.btnPi to "pi", R.id.btnE to "e",
            R.id.btnPerm to "P", R.id.btnComb to "C"
        )
        for ((id, token) in tokens) {
            view.findViewById<Button>(id).setOnClickListener { keypad.append(token) }
        }

        view.findViewById<Button>(R.id.btnEqual).setOnClickListener { keypad.evaluate() }
        view.findViewById<Button>(R.id.btnAC).setOnClickListener { keypad.clear() }
        view.findViewById<Button>(R.id.btnDel).setOnClickListener { keypad.backspace() }

        btnDeg = view.findViewById(R.id.btnDeg)
        btnDeg.setOnClickListener {
            degrees = !degrees
            applyAngleMode()
        }

        savedInstanceState?.let {
            degrees = it.getBoolean(KEY_DEG, false)
            it.getString(KEY_EXPR)?.let { e -> keypad.restore(e) }
        }
        applyAngleMode()
        return view
    }

    private fun applyAngleMode() {
        keypad.setAngleMode(if (degrees) AngleMode.DEGREES else AngleMode.RADIANS)
        btnDeg.text = getString(if (degrees) R.string.btn_deg else R.string.btn_rad)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::keypad.isInitialized) {
            outState.putString(KEY_EXPR, keypad.expr)
            outState.putBoolean(KEY_DEG, degrees)
        }
    }

    companion object {
        private const val KEY_EXPR = "sci_expr"
        private const val KEY_DEG = "sci_deg"
    }
}
