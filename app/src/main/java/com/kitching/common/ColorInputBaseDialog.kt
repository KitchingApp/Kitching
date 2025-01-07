package com.kitching.common

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.google.android.material.radiobutton.MaterialRadioButton
import com.kitching.R
import com.kitching.common.util.throttleClicks
import com.kitching.databinding.DialogInputTextColorBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

open class ColorInputBaseDialog(): BaseDialog<DialogInputTextColorBinding>(DialogInputTextColorBinding::inflate) {
    
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
                    background = ResourcesCompat.getDrawable(resources, R.drawable.circle_button, null)
                    backgroundTintList = ColorStateList.valueOf(Color.parseColor(it))
                    buttonDrawable = null
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                    setPadding(convertDpToPx(30), 0, convertDpToPx(30), 0)
                }
                colorPickerRG.addView(radioButton)
            }
        }
    }

    fun getCheckedColor(): String {
        with(binding) {
            return colorPickerRG.findViewById<RadioButton>(colorPickerRG.checkedRadioButtonId).tag.toString()
        }
    }

    fun getTextInput(): String {
        with(binding) {
            return textInputEditText.text.toString()
        }
    }

    private fun convertDpToPx(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
}