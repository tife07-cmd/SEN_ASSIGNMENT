package com.tife.tifescienticcalculator.feature.common

import android.widget.TextView
import com.tife.tifescienticcalculator.core.engine.AngleMode
import com.tife.tifescienticcalculator.core.engine.Calculator
import com.tife.tifescienticcalculator.core.format.NumberFormatter

/**
 * Holds the editable expression for a keypad screen and drives the two display lines.
 *
 * Buttons append engine-readable ASCII (e.g. "*", "sqrt("); the input line shows the prettified
 * form (×, √) so the surface stays readable while the engine still parses cleanly.
 */
class Keypad(
    private val inputView: TextView,
    private val resultView: TextView
) {
    var expr: String = ""
        private set

    private var calculator: Calculator = Calculator(AngleMode.RADIANS)

    fun setAngleMode(mode: AngleMode) {
        calculator = Calculator(mode)
        preview()
    }

    fun append(token: String) {
        expr += token
        render()
        preview()
    }

    fun clear() {
        expr = ""
        inputView.text = ""
        resultView.text = "0"
    }

    fun backspace() {
        if (expr.isEmpty()) return
        expr = dropLastToken(expr)
        render()
        preview()
    }

    fun evaluate() {
        if (expr.isBlank()) return
        resultView.text = try {
            NumberFormatter.format(calculator.evaluate(expr))
        } catch (e: Exception) {
            e.message ?: "Error"
        }
    }

    fun restore(saved: String) {
        expr = saved
        render()
        preview()
    }

    private fun preview() {
        if (expr.isBlank()) {
            resultView.text = "0"
            return
        }
        // a half-typed expression is normal mid-edit, so a failed preview just stays silent
        runCatching { resultView.text = NumberFormatter.format(calculator.evaluate(expr)) }
    }

    private fun render() {
        inputView.text = prettify(expr)
    }

    private fun prettify(s: String): String = s
        .replace("pi", "π")
        .replace("sqrt(", "√(")
        .replace("*", "×")
        .replace("/", "÷")
        .replace("-", "−")

    // a function token ("sin(") or constant ("pi") must be removed whole, not letter by letter
    private fun dropLastToken(s: String): String {
        val last = s.last()
        if (last == '(' || last.isLetter()) {
            var j = s.length - 1
            if (last == '(') j--
            while (j >= 0 && s[j].isLetter()) j--
            return s.substring(0, j + 1)
        }
        return s.dropLast(1)
    }
}
