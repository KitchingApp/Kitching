package com.kitching.view.fragment.other.scheduletime

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.google.android.material.radiobutton.MaterialRadioButton
import com.kitching.R
import com.kitching.common.BaseDialog
import com.kitching.common.firebaseResultHandler
import com.kitching.common.util.throttleClicks
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.databinding.DialogCreateScheduleTimeBinding
import com.kitching.view.model.ScheduleTimeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.widget.timeChangeEvents
import java.util.Locale

class ScheduleTimeUpdateDialog(): BaseDialog<DialogCreateScheduleTimeBinding>(
    DialogCreateScheduleTimeBinding::inflate) {

    private val viewModel = ScheduleTimeViewModel.instance

    private val args: ScheduleTimeUpdateDialogArgs by navArgs()

    private lateinit var teamId: String

    private lateinit var startTimeHour: String
    private lateinit var startTimeMinute: String
    private lateinit var endTimeHour: String
    private lateinit var endTimeMinute: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                teamId = PreferencesDataSource(requireContext()).getTeamId() ?: ""
            }
        }

        setColorPickerRG()

        with(binding) {
            scheduleTimeNameTI.setText(args.name)
            colorPickerRG.findViewWithTag<MaterialRadioButton>(args.color).isChecked = true
            startTimeTP.hour = args.startTime.split(":")[0].toInt()
            startTimeTP.minute = args.startTime.split(":")[1].toInt()
            endTimeTP.hour = args.endTime.split(":")[0].toInt()
            endTimeTP.minute = args.endTime.split(":")[1].toInt()

            with(confirmBtn) {
                text = "수정"

                throttleClicks(viewLifecycleOwner) {
                    viewModel.updateScheduleTime(args.scheduleTimeId, getTextInput(), getCheckedColor(), "$startTimeHour:$startTimeMinute", "$endTimeHour:$endTimeMinute")
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.updateScheduleTimeResult.collectLatest {
                            firebaseResultHandler(it) {
                                viewModel.getScheduleTimes(teamId)
                                dismiss()
                            }
                        }
                    }
                }
            }

            with(cancelBtn) {
                throttleClicks(viewLifecycleOwner) {
                    dismiss()
                }
            }

            getStartTimeFromTimePicker()
            getEndTimeFromTimePicker()
        }
    }

    private fun getStartTimeFromTimePicker() {
        binding.startTimeTP.timeChangeEvents().onEach {
            startTimeHour = String.format(Locale.KOREA, "%02d", it.hourOfDay)
            startTimeMinute = it.minute.toString()
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun getEndTimeFromTimePicker() {
        binding.endTimeTP.timeChangeEvents().onEach {
            endTimeHour = String.format(Locale.KOREA, "%02d", it.hourOfDay)
            endTimeMinute = it.minute.toString()
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setColorPickerRG() {
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
            binding.colorPickerRG.addView(radioButton)
        }
    }

    private fun getCheckedColor(): String {
        with(binding) {
            return colorPickerRG.findViewById<RadioButton>(colorPickerRG.checkedRadioButtonId).tag.toString()
        }
    }

    private fun getTextInput(): String {
        with(binding) {
            return scheduleTimeNameTI.text.toString()
        }
    }

    private fun convertDpToPx(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
}