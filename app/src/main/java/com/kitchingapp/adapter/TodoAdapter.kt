package com.kitchingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitchingapp.databinding.ItemBigCategoryBinding
import com.kitchingapp.databinding.ItemSmallCategoryBinding
import com.kitchingapp.domain.entities.Todo
import com.kitchingapp.domain.entities.TodoCategory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class TodoAdapter(private val lifecycleOwner: LifecycleOwner): ListAdapter<Todo, TodoAdapter.TodoViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemSmallCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bindBigCategory(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Todo>() {
            override fun areItemsTheSame(
                oldItem: Todo,
                newItem: Todo
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: Todo,
                newItem: Todo
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class TodoViewHolder(val binding: ItemSmallCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindBigCategory(todo: Todo) {
            with(binding) {
                categoryNameTV.text = todo.name
                categoryCV.clicks().onEach {

                }.launchIn(lifecycleOwner.lifecycleScope)
            }
        }
    }
}