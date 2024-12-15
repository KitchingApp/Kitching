package com.kitchingapp.view.fragment.schedule

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.kitchingapp.common.BaseDialog
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

        with(binding) {
            dateTV.text = LocalDate.now().toString()
            staffLevelTV.text = "직급: 매니저"
            continuousDateTV.text = "연속근무일수: 2일"

            // 타임별로 chip 생성
            createChip("A타임")

            with(confirmBtn) {
                clicks().onEach {
                    // 확인 버튼
                    dismiss()
                }.launchIn(lifecycleScope)

                text = "생성"
            }

            with(cancelBtn) {
                clicks().onEach {
                    dismiss()
                }.launchIn(lifecycleScope)

                text = "취소"
            }
        }
    }

    private fun createChip(chipName: String) {
        val chip = Chip(requireContext())
        chip.text = chipName
        binding.chipGroup.addView(chip)
    }
}