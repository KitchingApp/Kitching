package com.kitchingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitchingapp.databinding.ItemSmallCategoryBinding
import com.kitchingapp.domain.entities.StaffLevel

class StaffLevelAdapter(private val lifecycleOwner: LifecycleOwner) : ListAdapter<StaffLevel, StaffLevelAdapter.StaffLevelViewHolder>(diffUtil) {
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
        val diffUtil = object : DiffUtil.ItemCallback<StaffLevel>() {
            override fun areItemsTheSame(
                oldItem: StaffLevel,
                newItem: StaffLevel
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: StaffLevel,
                newItem: StaffLevel
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class StaffLevelViewHolder(val binding: ItemSmallCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindSmallCategory(staffLevel: StaffLevel) {
            with(binding) {
                categoryNameTV.text = staffLevel.name
            }
        }
    }
}