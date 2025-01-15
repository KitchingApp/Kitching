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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitching.R
import com.kitching.common.util.throttleFirst
import com.kitching.data.dto.ScheduleTimeListDTO
import com.kitching.databinding.ItemBigCategoryBinding
import com.kitching.view.fragment.other.scheduletime.ScheduleTimeFragmentDirections
import com.kitching.view.fragment.prep.PrepCategoryFragmentDirections
import com.kitching.view.model.ScheduleViewModel
import com.kitching.view.model.factory.FactoryScheduleViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.appcompat.itemClicks
import ru.ldralighieri.corbind.view.clicks

class ScheduleTimeAdapter(
    private val lifecycleOwner: LifecycleOwner
) : ListAdapter<ScheduleTimeListDTO, ScheduleTimeAdapter.ScheduleTimeViewHolder>(diffUtil) {

    private val viewModel = FactoryScheduleViewModel.fetchScheduleViewModel()

    private var navController: NavController? = null

    private lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScheduleTimeViewHolder {
        context = parent.context
        val binding = ItemBigCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        navController = Navigation.findNavController(parent)
        return ScheduleTimeViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ScheduleTimeViewHolder,
        position: Int
    ) {
        holder.bindBigCategory(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ScheduleTimeListDTO>() {
            override fun areItemsTheSame(
                oldItem: ScheduleTimeListDTO,
                newItem: ScheduleTimeListDTO
            ): Boolean {
                return oldItem.scheduleTimeName == newItem.scheduleTimeName && oldItem.color == newItem.color
            }

            override fun areContentsTheSame(
                oldItem: ScheduleTimeListDTO,
                newItem: ScheduleTimeListDTO
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ScheduleTimeViewHolder(val binding: ItemBigCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindBigCategory(scheduleTime: ScheduleTimeListDTO) {
            with(binding) {
                categoryNameTV.text = scheduleTime.scheduleTimeName
                categoryCV.setCardBackgroundColor(Color.parseColor(scheduleTime.color))
                optionBtn.clicks().throttleFirst().onEach {
                    showMenu(optionBtn, R.menu.option_menu, scheduleTime.scheduleTimeId, scheduleTime.scheduleTimeName, scheduleTime.color, scheduleTime.startTime, scheduleTime.endTime)
                }.launchIn(lifecycleOwner.lifecycleScope)
            }
        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int, scheduleId: String, name: String, color: String, startTime: String, endTime: String) {
        val popup = PopupMenu(context, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.itemClicks().onEach {
            when(it.itemId) {
                R.id.updateInOptionMenu -> {
                    val action = ScheduleTimeFragmentDirections.actionScheduleTimeFragmentToScheduleTimeUpdateDialog(scheduleId, name, color, startTime, endTime)
                    navController?.navigate(action)
                }
                R.id.deleteInOptionMenu -> {
                    val action = ScheduleTimeFragmentDirections.actionScheduleTimeFragmentToScheduleTimeDeleteDialog(scheduleId)
                    navController?.navigate(action)
                }
            }
        }.launchIn(lifecycleOwner.lifecycleScope)

        popup.show()
    }
}