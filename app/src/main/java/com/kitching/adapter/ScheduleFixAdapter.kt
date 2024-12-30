package com.kitching.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitching.adapter.ScheduleFixAdapter.ScheduleViewHolder
import com.kitching.data.dto.ScheduleDTO
import com.kitching.databinding.ItemScheduleListBinding

class ScheduleFixAdapter : ListAdapter<ScheduleDTO, ScheduleViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScheduleViewHolder {
        val binding = ItemScheduleListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ScheduleViewHolder,
        position: Int
    ) {
        holder.bindScheduleFix(currentList[position])
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
                }
            }

    }
}