package com.takasima.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.takasima.calculatorapp.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    var lastIsNumeric = false
    var lastIsDot = false
    var stateError = false

    private lateinit var expression: Expression
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun onNumberClick(view: View) {
        if (stateError) {
            binding.tvData.text = (view as Button).text
            stateError = false
        } else {
            binding.tvData.append((view as Button).text)
        }

        lastIsNumeric = true
        onEqual()
    }



    fun onOperatorClick(view: View) {
        if (!stateError && lastIsNumeric) {
            binding.tvData.append((view as Button).text)
            lastIsNumeric = false
            lastIsDot = false

            onEqual()
        }
    }
    fun onBackClick(view: View) {
        binding.tvData.text = binding.tvData.text.dropLast(1)

        try {
            val lastChar = binding.tvData.text.toString().last()

            if (lastChar.isDigit()) {
                onEqual()
            }
        } catch (e: Exception) {
            binding.tvResult.text = ""
            binding.tvResult.visibility = View.GONE
            Log.e("last char error", e.toString())
        }
    }
    fun onAllClearClick(view: View) {
        binding.tvData.text = ""
        binding.tvResult.text = ""
        lastIsNumeric = false
        lastIsDot = false
        stateError = false
        binding.tvResult.visibility = View.GONE
    }
    fun onClearClick(view: View) {
        binding.tvData.text = ""
        lastIsNumeric = false
    }
    fun onEqualClick(view: View) {
       onEqual()
        binding.tvData.text = binding.tvResult.text.toString().drop(1)
    }

    fun onEqual() {
        if (lastIsNumeric && !stateError) {
            val txt = binding.tvData.text.toString()
            expression = ExpressionBuilder(txt).build()

            try {
                val result = expression.evaluate()
                binding.tvResult.visibility = View.VISIBLE

                binding.tvResult.text = "=" + result.toString()
            } catch (e: ArithmeticException) {
                Log.e("evaluate error", e.toString())
                binding.tvResult.text = "Error"
                stateError = true
                lastIsNumeric = false
            }
        }
    }


}