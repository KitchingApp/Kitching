package com.kitching.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitching.data.dto.ScheduleDTO
import com.kitching.databinding.ItemScheduleApplylistBinding

class ScheduleApplyAdapter: ListAdapter<ScheduleDTO, ScheduleApplyAdapter.ScheduleViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val binding = ItemScheduleApplylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bindScheduleApply(currentList[position])
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

    inner class ScheduleViewHolder(val binding: ItemScheduleApplylistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindScheduleApply(schedule: ScheduleDTO) {
            with(binding) {
                scheduleApplyNameTV.text = schedule.userName
                scheduleApplyTimeTV.text = schedule.scheduleTimeName
            }
        }

    }
}