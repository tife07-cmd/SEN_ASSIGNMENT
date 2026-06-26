package com.tife.tifescienticcalculator.feature.statistics

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
import com.tife.tifescienticcalculator.core.format.NumberFormatter
import com.tife.tifescienticcalculator.core.math.Statistics

class StatisticsFragment : Fragment() {

    private lateinit var input: EditText
    private lateinit var output: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)

        input = view.findViewById(R.id.inputData)
        output = view.findViewById(R.id.tvStatsOutput)

        view.findViewById<Button>(R.id.btnCompute).setOnClickListener { compute() }
        return view
    }

    private fun compute() {
        val data = input.text.toString()
            .split(",", " ", "\n")
            .filter { it.isNotBlank() }
            .mapNotNull { it.trim().toDoubleOrNull() }

        output.text = try {
            val fmt = NumberFormatter::format
            buildString {
                appendLine("count         ${data.size}")
                appendLine("mean          ${fmt(Statistics.mean(data))}")
                appendLine("median        ${fmt(Statistics.median(data))}")
                appendLine("mode          ${Statistics.mode(data).joinToString(", ") { fmt(it) }}")
                appendLine("range         ${fmt(Statistics.range(data))}")
                appendLine("sample sd     ${fmt(Statistics.stdDev(data, true))}")
                append("population sd ${fmt(Statistics.stdDev(data, false))}")
            }
        } catch (e: CalcException) {
            e.message ?: getString(R.string.error_msg)
        }
    }
}
