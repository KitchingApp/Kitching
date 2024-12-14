package com.kitchingapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitchingapp.R
import com.kitchingapp.databinding.ItemBigCategoryBinding
import com.kitchingapp.databinding.ItemSmallCategoryBinding
import com.kitchingapp.domain.entities.TodoCategory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class TodoCategoryAdapter(private val lifecycleOwner: LifecycleOwner, private val navController: NavController): ListAdapter<TodoCategory, TodoCategoryAdapter.TodoCategoryViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoCategoryViewHolder {
        val binding = ItemBigCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoCategoryViewHolder, position: Int) {
        holder.bindBigCategory(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<TodoCategory>() {
            override fun areItemsTheSame(
                oldItem: TodoCategory,
                newItem: TodoCategory
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: TodoCategory,
                newItem: TodoCategory
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class TodoCategoryViewHolder(val binding: ItemBigCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindBigCategory(todoCategory: TodoCategory) {
            with(binding) {
                categoryNameTV.text = todoCategory.name
                categoryCV.setCardBackgroundColor(todoCategory.color)
                categoryCV.clicks().onEach {
                    navController.navigate(R.id.action_todoFragment_to_todoListFragment)
                }.launchIn(lifecycleOwner.lifecycleScope)
            }
        }
    }
}