package com.kitchingapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitchingapp.R
import com.kitchingapp.common.throttleFirst
import com.kitchingapp.data.database.dto.DepartmentDTO
import com.kitchingapp.databinding.ItemBigCategoryBinding
import com.kitchingapp.view.fragment.other.DepartmentFragmentDirections
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class DepartmentAdapter(private val lifecycleOwner: LifecycleOwner, private val navController: NavController): ListAdapter<DepartmentDTO, DepartmentAdapter.DepartmentViewHolder>(diffUtil) {
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
        val diffUtil = object : DiffUtil.ItemCallback<DepartmentDTO>() {
            override fun areItemsTheSame(
                oldItem: DepartmentDTO,
                newItem: DepartmentDTO
            ): Boolean {
                return oldItem.departmentId == newItem.departmentId
            }

            override fun areContentsTheSame(
                oldItem: DepartmentDTO,
                newItem: DepartmentDTO
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class DepartmentViewHolder(val binding: ItemBigCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindBigCategory(department: DepartmentDTO) {
            with(binding) {
                categoryNameTV.text = department.departmentName
                categoryCV.setCardBackgroundColor(Color.parseColor(department.color))
                categoryCV.clicks().throttleFirst().onEach {
                    val argActions = DepartmentFragmentDirections.actionDepartmentFragmentToStaffLevelFragment(department.departmentId)
                    navController.navigate(argActions)
                }.launchIn(lifecycleOwner.lifecycleScope)
            }
        }
    }
}