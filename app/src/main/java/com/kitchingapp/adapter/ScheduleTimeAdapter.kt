package com.kitchingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitchingapp.data.database.dto.ScheduleTimeDTO
import com.kitchingapp.databinding.ItemBigCategoryBinding

class ScheduleTimeAdapter : ListAdapter<ScheduleTimeDTO, ScheduleTimeAdapter.ScheduleTimeViewHolder>(diffUtil) {
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
        val diffUtil = object : DiffUtil.ItemCallback<ScheduleTimeDTO>() {
            override fun areItemsTheSame(
                oldItem: ScheduleTimeDTO,
                newItem: ScheduleTimeDTO
            ): Boolean {
                return oldItem.scheduleTimeName == newItem.scheduleTimeName
            }

            override fun areContentsTheSame(
                oldItem: ScheduleTimeDTO,
                newItem: ScheduleTimeDTO
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ScheduleTimeViewHolder(val binding: ItemBigCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindBigCategory(scheduleTime: ScheduleTimeDTO) {
            with(binding) {
                categoryNameTV.text = scheduleTime.scheduleTimeName
            }
        }
    }
}