package com.kitching.view.fragment.schedule

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.kitching.common.BaseDialog
import com.kitching.common.firebaseResultHandler
import com.kitching.common.util.throttleFirst
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.data.dto.DropDownMembersDTO
import com.kitching.data.dto.ScheduleTimeChipsDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.ScheduleRepository
import com.kitching.databinding.DialogCreateScheduleBinding
import com.kitching.view.model.ScheduleViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.material.checkedChanges
import ru.ldralighieri.corbind.view.clicks
import ru.ldralighieri.corbind.widget.itemClickEvents

class ScheduleCreateDialog :
    BaseDialog<DialogCreateScheduleBinding>(DialogCreateScheduleBinding::inflate) {

//    private val viewModel by viewModels<ScheduleViewModel> {
//        viewModelFactory
//    }

    private val viewModel = ScheduleViewModel.instance

    private val args: ScheduleCreateDialogArgs by navArgs()

    private lateinit var teamId: String
    private lateinit var userId: String
    private lateinit var scheduleTimeId: String

    inner class DropDownAdapter(
        private val dataList: List<DropDownMembersDTO>
    ) : ArrayAdapter<DropDownMembersDTO>(
        requireContext(),
        android.R.layout.simple_dropdown_item_1line,
        dataList
    ) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getView(position, convertView, parent)

            val member = dataList[position]

            (view as TextView).text = member.userName

            return view
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                teamId = PreferencesDataSource(requireContext()).getTeamId() ?: ""
                viewModel.getMembers(teamId)
                viewModel.getScheduleTimes(teamId)

                launch {
                    viewModel.members.collectLatest { members ->
                        firebaseResultHandler(members) { data ->
                            if (data.isNotEmpty()) {
                                with(binding.autoCompleteTV) {
                                    setAdapter(DropDownAdapter(data))
                                }
                            }
                        }
                    }
                }

                launch {
                    viewModel.scheduleTimes.collectLatest { scheduleTimes ->
                        firebaseResultHandler(scheduleTimes) { data ->
                            if (data.isNotEmpty()) {
                                createChips(data)
                            }
                        }
                    }
                }
            }
        }

        with(binding) {
            dateTV.text = args.dateString
//            continuousDateTV.text = "연속근무일수: 2일"

            autoCompleteTV.itemClickEvents().throttleFirst().onEach { event ->
                val selectedMember =
                    autoCompleteTV.adapter.getItem(event.position) as DropDownMembersDTO
                autoCompleteTV.setText(selectedMember.userName)
                userId = selectedMember.userId
            }.launchIn(lifecycleScope)

            with(confirmBtn) {
                text = "배정"
                clicks().throttleFirst().onEach {
                    viewModel.createSchedule(teamId, args.dateString, userId, scheduleTimeId)
                    viewModel.createScheduleResult.collectLatest {
                        firebaseResultHandler(it) {
                            viewModel.getSchedules(teamId, args.dateString)
                            dismiss()
                        }
                    }
                }.launchIn(lifecycleScope)
            }

            with(cancelBtn) {
                clicks().throttleFirst().onEach {
                    dismiss()
                }.launchIn(lifecycleScope)

                text = "취소"
            }

            with(chipGroup) {
                checkedChanges().throttleFirst().onEach { checkedIds ->
                    val selectedChip = checkedIds.firstOrNull()?.let { findViewById<Chip>(it) }
                    scheduleTimeId = selectedChip?.tag.toString()
                }.launchIn(lifecycleScope)
            }
        }
    }

    private fun createChips(scheduleTimes: List<ScheduleTimeChipsDTO>) {
        scheduleTimes.forEach {
            val chip = Chip(requireContext())
            chip.text = it.scheduleTimeName
            chip.tag = it.scheduleTimeId
            chip.isCheckable = true
            chip.isChecked = false
            binding.chipGroup.addView(chip)
        }
    }
}