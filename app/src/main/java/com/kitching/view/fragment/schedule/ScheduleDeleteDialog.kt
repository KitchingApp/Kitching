package com.kitching.view.fragment.schedule

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.kitching.common.BaseDialog
import com.kitching.common.util.throttleClicks
import com.kitching.common.util.throttleFirst
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.data.dto.DropDownMembersDTO
import com.kitching.data.dto.ScheduleTimeChipsDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.ScheduleRepository
import com.kitching.databinding.DialogConfirmBinding
import com.kitching.databinding.DialogCreateScheduleBinding
import com.kitching.view.model.ScheduleViewModel
import com.kitching.view.model.factory.viewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.material.checkedChanges
import ru.ldralighieri.corbind.view.clicks
import ru.ldralighieri.corbind.widget.itemClickEvents

class ScheduleDeleteDialog:
    BaseDialog<DialogConfirmBinding>(DialogConfirmBinding::inflate) {

//    private val viewModel by viewModels<ScheduleViewModel> {
//        viewModelFactory
//    }

    private val viewModel = ScheduleViewModel.instance

    private val args: ScheduleDeleteDialogArgs by navArgs()

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
                dismiss()
            }
        }

    }
}