package com.kitching.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitching.common.throttleFirst
import com.kitching.data.dto.PrepDTO
import com.kitching.databinding.ItemSmallCategoryBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class PrepAdapter(private val lifecycleOwner: LifecycleOwner): ListAdapter<PrepDTO, PrepAdapter.PrepViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrepViewHolder {
        val binding = ItemSmallCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PrepViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PrepViewHolder, position: Int) {
        holder.bindBigCategory(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<PrepDTO>() {
            override fun areItemsTheSame(
                oldItem: PrepDTO,
                newItem: PrepDTO
            ): Boolean {
                return oldItem.prepName == newItem.prepName
            }

            override fun areContentsTheSame(
                oldItem: PrepDTO,
                newItem: PrepDTO
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class PrepViewHolder(val binding: ItemSmallCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindBigCategory(todo: PrepDTO) {
            with(binding) {
                categoryNameTV.text = todo.prepName
                categoryCV.clicks().throttleFirst().onEach {

                }.launchIn(lifecycleOwner.lifecycleScope)
            }
        }
    }
}