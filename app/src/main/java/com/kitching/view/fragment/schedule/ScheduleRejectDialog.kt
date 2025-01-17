package com.kitching.view.fragment.schedule

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.kitching.common.BaseDialog
import com.kitching.common.KitchingApplication
import com.kitching.common.firebaseResultHandler
import com.kitching.common.util.throttleClicks
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.databinding.DialogInputTextBinding
import com.kitching.view.model.ScheduleViewModel
import kotlinx.coroutines.launch

class ScheduleRejectDialog:
    BaseDialog<DialogInputTextBinding>(DialogInputTextBinding::inflate) {

    private val viewModel = ScheduleViewModel.instance

    private val args: ScheduleDeleteDialogArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            textField.hint = "신청 거절 사유를 입력해주세요."

            with(confirmBtn) {
                text = "거절"
                throttleClicks(viewLifecycleOwner) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        val teamId = PreferencesDataSource(KitchingApplication.getAppContext()).getTeamId() ?: ""
                        viewModel.deleteSchedule(args.scheduleId, true)
                        viewModel.scheduleResult.collect {
                            firebaseResultHandler(it) {
                                viewModel.getSchedules(teamId, args.dateString)
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
        }
    }
}