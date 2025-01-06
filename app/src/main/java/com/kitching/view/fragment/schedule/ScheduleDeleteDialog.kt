package com.kitching.view.fragment.schedule

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.kitching.common.BaseDialog
import com.kitching.common.util.throttleClicks
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.data.repository.ScheduleRepository
import com.kitching.databinding.DialogConfirmBinding
import com.kitching.view.model.ScheduleViewModel
import kotlinx.coroutines.launch

class ScheduleDeleteDialog:
    BaseDialog<DialogConfirmBinding>(DialogConfirmBinding::inflate) {

//    private val viewModel by viewModels<ScheduleViewModel> {
//        viewModelFactory
//    }

    private val viewModel = ScheduleViewModel.instance

    private val args: ScheduleDeleteDialogArgs by navArgs()

    override fun onAttach(context: Context) {
        Log.d("deleteDialog", "onAttach")
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            messageTV.text = "스케줄을 삭제하시겠습니까?"

            with(confirmButton) {
                text = "삭제"
                throttleClicks(viewLifecycleOwner) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        val teamId = PreferencesDataSource(context).getTeamId() ?: ""
                        ScheduleRepository().deleteSchedule(args.scheduleId)
                        viewModel.getSchedules(teamId, args.dateString)
                        dismiss()
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

    override fun onDismiss(dialog: DialogInterface) {
        Log.d("deleteDialog", "onDismiss")
        super.onDismiss(dialog)
    }
}