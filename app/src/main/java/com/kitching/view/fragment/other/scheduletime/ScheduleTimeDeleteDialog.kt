package com.kitching.view.fragment.other.scheduletime

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.kitching.common.BaseDialog
import com.kitching.common.KitchingApplication
import com.kitching.common.firebaseResultHandler
import com.kitching.common.util.throttleClicks
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.databinding.DialogConfirmBinding
import com.kitching.view.model.DepartmentViewModel
import com.kitching.view.model.ScheduleTimeViewModel
import com.kitching.view.model.ScheduleViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ScheduleTimeDeleteDialog:
    BaseDialog<DialogConfirmBinding>(DialogConfirmBinding::inflate) {

    private val viewModel = ScheduleTimeViewModel.instance

    private lateinit var teamId: String

    private val args: ScheduleTimeDeleteDialogArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            messageTV.text = "타임을 삭제하시겠습니까?"

            with(confirmButton) {
                text = "삭제"
                throttleClicks(viewLifecycleOwner) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        teamId = PreferencesDataSource(requireContext()).getTeamId() ?: ""
                        viewModel.deleteScheduleTime(args.scheduleTimeId)
                        viewModel.deleteScheduleTimeResult.collectLatest {
                            firebaseResultHandler(it) {
                                viewModel.getScheduleTimes(teamId)
                                dismiss()
                            }
                        }
                    }
                }
            }

            with(cancelButton) {
                throttleClicks(viewLifecycleOwner) {
                    dismiss()
                }
            }
        }
    }
}