package com.kitchingapp.adapter

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
import com.kitchingapp.domain.entities.Department
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class DepartmentAdapter(private val lifecycleOwner: LifecycleOwner, private val navController: NavController): ListAdapter<Department, DepartmentAdapter.DepartmentViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DepartmentViewHolder {
        val binding = ItemBigCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DepartmentViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DepartmentViewHolder,
        position: Int
    ) {
        holder.bindBigCategory(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Department>() {
            override fun areItemsTheSame(
                oldItem: Department,
                newItem: Department
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: Department,
                newItem: Department
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class DepartmentViewHolder(val binding: ItemBigCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindBigCategory(department: Department) {
            with(binding) {
                categoryNameTV.text = department.name
                categoryCV.setCardBackgroundColor(department.color)
                categoryCV.clicks().onEach {
                    navController.navigate(R.id.action_humanResourceFragment_to_departmentFragment)
                }.launchIn(lifecycleOwner.lifecycleScope)
            }
        }
    }
}