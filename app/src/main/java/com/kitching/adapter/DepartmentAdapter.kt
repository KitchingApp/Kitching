package com.kitching.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitching.R
import com.kitching.common.util.throttleClicks
import com.kitching.common.util.throttleFirst
import com.kitching.data.dto.DepartmentDTO
import com.kitching.databinding.ItemBigCategoryBinding
import com.kitching.view.fragment.other.department.DepartmentFragmentDirections
import com.kitching.view.fragment.prep.PrepCategoryFragmentDirections
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.appcompat.itemClicks
import ru.ldralighieri.corbind.view.clicks

class DepartmentAdapter(private val context: Context, private val lifecycleOwner: LifecycleOwner, private val navController: NavController): ListAdapter<DepartmentDTO, DepartmentAdapter.DepartmentViewHolder>(diffUtil) {
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
                categoryCV.throttleClicks(lifecycleOwner) {
                    val argActions = DepartmentFragmentDirections.actionDepartmentFragmentToStaffLevelFragment(department.departmentId)
                    navController.navigate(argActions)
                }
                optionBtn.clicks().throttleFirst().onEach {
                    showMenu(optionBtn, R.menu.option_menu, department.departmentId, department.departmentName, department.color)
                }.launchIn(lifecycleOwner.lifecycleScope)
            }
        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int, departmentId: String, name: String, color: String) {
        val popup = PopupMenu(context, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.itemClicks().onEach {
            when(it.itemId) {
                R.id.updateInOptionMenu -> {
                    val action = DepartmentFragmentDirections.actionDepartmentFragmentToDepartmentUpdateDialog(departmentId, name, color)
                    navController.navigate(action)
                }
                R.id.deleteInOptionMenu -> {
                    val action = DepartmentFragmentDirections.actionDepartmentFragmentToDepartmentDeleteDialog(departmentId)
                    navController.navigate(action)
                }
            }
        }.launchIn(lifecycleOwner.lifecycleScope)

        popup.show()
    }
}