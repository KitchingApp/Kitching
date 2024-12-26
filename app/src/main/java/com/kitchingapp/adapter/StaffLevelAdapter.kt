package com.kitchingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitchingapp.data.database.dto.StaffLevelDTO
import com.kitchingapp.databinding.ItemSmallCategoryBinding

class StaffLevelAdapter(private val lifecycleOwner: LifecycleOwner) : ListAdapter<StaffLevelDTO, StaffLevelAdapter.StaffLevelViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StaffLevelViewHolder {
        val binding = ItemSmallCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StaffLevelViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: StaffLevelViewHolder,
        position: Int
    ) {
        holder.bindSmallCategory(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<StaffLevelDTO>() {
            override fun areItemsTheSame(
                oldItem: StaffLevelDTO,
                newItem: StaffLevelDTO
            ): Boolean {
                return oldItem.staffLevelId == newItem.staffLevelId
            }

            override fun areContentsTheSame(
                oldItem: StaffLevelDTO,
                newItem: StaffLevelDTO
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class StaffLevelViewHolder(val binding: ItemSmallCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindSmallCategory(staffLevel: StaffLevelDTO) {
            with(binding) {
                categoryNameTV.text = staffLevel.staffLevelName
            }
        }
    }
}