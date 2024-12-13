package com.kitchingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitchingapp.databinding.ItemScheduleApplylistBinding
import com.kitchingapp.domain.entities.Schedule

class ScheduleApplyAdapter: ListAdapter<Schedule, ScheduleApplyAdapter.ScheduleViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val binding = ItemScheduleApplylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bindScheduleApply(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Schedule>() {
            override fun areItemsTheSame(
                oldItem: Schedule,
                newItem: Schedule
            ): Boolean {
                return oldItem.applier == newItem.applier
            }

            override fun areContentsTheSame(
                oldItem: Schedule,
                newItem: Schedule
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ScheduleViewHolder(val binding: ItemScheduleApplylistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindScheduleApply(schedule: Schedule) {
            with(binding) {
                scheduleApplyNameTV.text = schedule.applier.name
                scheduleApplyTimeTV.text = schedule.scheduleTime.name
            }
        }

    }
}