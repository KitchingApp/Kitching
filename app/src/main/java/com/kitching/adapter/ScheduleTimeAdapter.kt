package com.kitching.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitching.data.dto.ScheduleTimeListDTO
import com.kitching.databinding.ItemBigCategoryBinding

class ScheduleTimeAdapter : ListAdapter<ScheduleTimeListDTO, ScheduleTimeAdapter.ScheduleTimeViewHolder>(diffUtil) {
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
        val diffUtil = object : DiffUtil.ItemCallback<ScheduleTimeListDTO>() {
            override fun areItemsTheSame(
                oldItem: ScheduleTimeListDTO,
                newItem: ScheduleTimeListDTO
            ): Boolean {
                return oldItem.scheduleTimeName == newItem.scheduleTimeName
            }

            override fun areContentsTheSame(
                oldItem: ScheduleTimeListDTO,
                newItem: ScheduleTimeListDTO
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ScheduleTimeViewHolder(val binding: ItemBigCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindBigCategory(scheduleTime: ScheduleTimeListDTO) {
            with(binding) {
                categoryNameTV.text = scheduleTime.scheduleTimeName
            }
        }
    }
}