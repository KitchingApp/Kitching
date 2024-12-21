package com.kitchingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitchingapp.data.database.dto.PrepDTO
import com.kitchingapp.databinding.ItemSmallCategoryBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class TodoAdapter(private val lifecycleOwner: LifecycleOwner): ListAdapter<PrepDTO, TodoAdapter.TodoViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemSmallCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
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

    inner class TodoViewHolder(val binding: ItemSmallCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindBigCategory(todo: PrepDTO) {
            with(binding) {
                categoryNameTV.text = todo.prepName
                categoryCV.clicks().onEach {

                }.launchIn(lifecycleOwner.lifecycleScope)
            }
        }
    }
}