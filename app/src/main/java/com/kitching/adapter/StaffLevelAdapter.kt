package com.kitching.adapter

import android.content.Context
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
import com.kitching.data.dto.StaffLevelDTO
import com.kitching.databinding.ItemSmallCategoryBinding
import com.kitching.view.fragment.other.department.DepartmentFragmentDirections
import com.kitching.view.fragment.other.department.StaffLevelFragment
import com.kitching.view.fragment.other.department.StaffLevelFragmentDirections
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.appcompat.itemClicks

class StaffLevelAdapter(private val context: Context, private val lifecycleOwner: LifecycleOwner, private val navController: NavController) : ListAdapter<StaffLevelDTO, StaffLevelAdapter.StaffLevelViewHolder>(diffUtil) {
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

    private fun showMenu(v: View, @MenuRes menuRes: Int, staffLevelId: String, name: String) {
        val popup = PopupMenu(context, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.itemClicks().onEach {
            when(it.itemId) {
                R.id.updateInOptionMenu -> {
                    val action = StaffLevelFragmentDirections.actionStaffLevelFragmentToStaffLevelUpdateDialog(staffLevelId, name)
                    navController.navigate(action)
                }
                R.id.deleteInOptionMenu -> {
                    val action = StaffLevelFragmentDirections.actionStaffLevelFragmentToStaffLevelDeleteDialog(staffLevelId)
                    navController.navigate(action)
                }
            }
        }.launchIn(lifecycleOwner.lifecycleScope)

        popup.show()
    }
}