package com.kitching.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitching.adapter.ScheduleFixAdapter.ScheduleViewHolder
import com.kitching.common.util.throttleClicks
import com.kitching.data.dto.ScheduleDTO
import com.kitching.databinding.ItemScheduleListBinding
import com.kitching.view.fragment.schedule.ScheduleFragmentDirections

class ScheduleFixAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val currentDate: String
) : ListAdapter<ScheduleDTO, ScheduleViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScheduleViewHolder {
        val binding =
            ItemScheduleListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        navController = Navigation.findNavController(parent)
        return ScheduleViewHolder(binding)
    }

    private var navController: NavController? = null

    override fun onBindViewHolder(
        holder: ScheduleViewHolder,
        position: Int
    ) {
        holder.bindScheduleFix(currentList[position])
    }

    override fun onCurrentListChanged(
        previousList: MutableList<ScheduleDTO>,
        currentList: MutableList<ScheduleDTO>
    ) {
        super.onCurrentListChanged(previousList, currentList)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ScheduleDTO>() {
            override fun areItemsTheSame(
                oldItem: ScheduleDTO,
                newItem: ScheduleDTO
            ): Boolean {
                return oldItem.scheduleId == newItem.scheduleId
            }

            override fun areContentsTheSame(
                oldItem: ScheduleDTO,
                newItem: ScheduleDTO
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ScheduleViewHolder(val binding: ItemScheduleListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindScheduleFix(schedule: ScheduleDTO) {
            with(binding) {
                scheduleNameTV.text = schedule.userName
                scheduleTimeTV.text = schedule.scheduleTimeName

                rejectBtn.throttleClicks(lifecycleOwner) {
                    val action =
                        ScheduleFragmentDirections.actionScheduleFragmentToScheduleDeleteDialog(
                            dateString = currentDate,
                            scheduleId = schedule.scheduleId,
                        )
                    navController?.navigate(action)
                }
            }
        }
    }
}