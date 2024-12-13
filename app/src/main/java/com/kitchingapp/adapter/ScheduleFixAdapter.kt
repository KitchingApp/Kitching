package com.kitchingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.protobuf.Internal
import com.kitchingapp.adapter.ScheduleFixAdapter.ScheduleViewHolder
import com.kitchingapp.databinding.ItemScheduleListBinding
import com.kitchingapp.domain.entities.RestaurantTeam
import com.kitchingapp.domain.entities.Schedule

class ScheduleFixAdapter : ListAdapter<Schedule, ScheduleViewHolder>(diffUtil) {
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

    inner class ScheduleViewHolder(val binding: ItemScheduleListBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bindScheduleFix(schedule: Schedule) {
                with(binding) {
                    scheduleNameTV.text = schedule.applier.name
                    scheduleTimeTV.text = schedule.scheduleTime.name
                    scheduleContinuousDateTV.text = "연속근무일수 1일"
                }
            }

    }
}