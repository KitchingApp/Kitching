package com.kitchingapp.view.fragment.schedule

import android.os.Build
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.kitchingapp.R
import com.kitchingapp.common.BaseDialog
import com.kitchingapp.common.throttleFirst
import com.kitchingapp.databinding.DialogCreateScheduleBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import java.time.LocalDate

class ScheduleCreateDialog: BaseDialog<DialogCreateScheduleBinding>(DialogCreateScheduleBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val staffLevelListMockData = listOf("사원", "주임", "대리", "과장", "차장")
        val scheduleTimeListMockData = listOf("A타임", "B타임", "C타임")

        val autoCompleteAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, staffLevelListMockData)

        with(binding) {
            dateTV.text = LocalDate.now().toString()
            continuousDateTV.text = "연속근무일수: 2일"
            autoCompleteTV.setAdapter(autoCompleteAdapter)

            // 타임별로 chip 생성
            scheduleTimeListMockData.forEach {
                createChip(it)
            }

            with(confirmBtn) {
                clicks().throttleFirst().onEach {
                    // 확인 버튼
                    dismiss()
                }.launchIn(lifecycleScope)

                text = "생성"
            }

            with(cancelBtn) {
                clicks().throttleFirst().onEach {
                    dismiss()
                }.launchIn(lifecycleScope)

                text = "취소"
            }
        }
    }

    private fun createChip(chipName: String) {
        val chip = Chip(requireContext())
        chip.text = chipName
        chip.isCheckable = true
        binding.chipGroup.addView(chip)
    }
}