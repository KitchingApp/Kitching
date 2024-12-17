package com.kitchingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitchingapp.databinding.ItemBigCategoryBinding
import com.kitchingapp.domain.entities.ScheduleTime

class ScheduleTimeAdapter : ListAdapter<ScheduleTime, ScheduleTimeAdapter.ScheduleTimeViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScheduleTimeViewHolder {
        val binding = ItemBigCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScheduleTimeViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ScheduleTimeViewHolder,
        position: Int
    ) {
        holder.bindBigCategory(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ScheduleTime>() {
            override fun areItemsTheSame(
                oldItem: ScheduleTime,
                newItem: ScheduleTime
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: ScheduleTime,
                newItem: ScheduleTime
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ScheduleTimeViewHolder(val binding: ItemBigCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindBigCategory(scheduleTime: ScheduleTime) {
            with(binding) {
                categoryNameTV.text = scheduleTime.name
            }
        }
    }
}