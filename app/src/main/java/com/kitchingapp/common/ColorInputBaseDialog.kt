package com.kitchingapp.common

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.google.android.material.radiobutton.MaterialRadioButton
import com.kitchingapp.R
import com.kitchingapp.databinding.DialogInputTextColorBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

open class ColorInputBaseDialog: BaseDialog<DialogInputTextColorBinding>(DialogInputTextColorBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val colors = listOf("#EF9A9A", "#F48FB1", "#CE93D8", "#B39DDB", "#9FA8DA", "#90CAF9", "#81D4FA")

            colors.forEach{
                val radioButton = MaterialRadioButton(requireContext()).apply {
                    tag = it
                    width = convertDpToPx(50)
                    height = convertDpToPx(50)
                    background = resources.getDrawable(R.drawable.circle_button, null)
                    backgroundTintList = ColorStateList.valueOf(Color.parseColor(it))
                    buttonDrawable = null
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                    setPadding(convertDpToPx(30), 0, convertDpToPx(30), 0)
                }
                colorPickerRG.addView(radioButton)
            }
        }
    }

    private fun convertDpToPx(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
}