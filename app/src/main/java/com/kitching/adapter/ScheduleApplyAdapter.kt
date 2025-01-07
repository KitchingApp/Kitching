package com.kitching.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitching.common.KitchingApplication
import com.kitching.common.util.throttleClicks
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.data.dto.ScheduleDTO
import com.kitching.databinding.ItemScheduleApplylistBinding
import com.kitching.view.fragment.schedule.ScheduleFragmentDirections
import com.kitching.view.model.ScheduleViewModel
import kotlinx.coroutines.launch

class ScheduleApplyAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val currentDate: String
): ListAdapter<ScheduleDTO, ScheduleApplyAdapter.ScheduleViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val binding = ItemScheduleApplylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        navController = Navigation.findNavController(parent)
        return ScheduleViewHolder(binding)
    }

    private val viewModel = ScheduleViewModel.instance

    private var navController: NavController? = null


    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bindScheduleApply(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ScheduleDTO>() {
            override fun areItemsTheSame(
                oldItem: ScheduleDTO,
                newItem: ScheduleDTO
            ): Boolean {
                return oldItem.scheduleId == newItem.scheduleId && oldItem.isFix == newItem.isFix
            }

            override fun areContentsTheSame(
                oldItem: ScheduleDTO,
                newItem: ScheduleDTO
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ScheduleViewHolder(val binding: ItemScheduleApplylistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindScheduleApply(schedule: ScheduleDTO) {
            with(binding) {
                scheduleApplyNameTV.text = schedule.userName
                scheduleApplyTimeTV.text = schedule.scheduleTimeName
                var teamId = ""

                applyBtn.throttleClicks(lifecycleOwner) {
                    lifecycleOwner.lifecycleScope.launch {
                        teamId = PreferencesDataSource(KitchingApplication.getAppContext()).getTeamId() ?: ""
                        viewModel.applySchedule(schedule.scheduleId)
                        viewModel.getSchedules(teamId, currentDate)
                    }
                }

                rejectBtn.throttleClicks(lifecycleOwner) {
                    val action =
                        ScheduleFragmentDirections.actionScheduleFragmentToScheduleRejectDialog(
                            dateString = currentDate,
                            scheduleId = schedule.scheduleId,
                        )
                    navController?.navigate(action)
                }
            }
        }
    }
}