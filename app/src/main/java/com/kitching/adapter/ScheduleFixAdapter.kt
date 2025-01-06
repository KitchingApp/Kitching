package com.kitching.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitching.adapter.ScheduleFixAdapter.ScheduleViewHolder
import com.kitching.common.util.throttleClicks
import com.kitching.common.util.throttleFirst
import com.kitching.data.dto.ScheduleDTO
import com.kitching.databinding.FragmentScheduleBinding
import com.kitching.databinding.ItemScheduleListBinding
import com.kitching.view.fragment.schedule.ScheduleFragment
import com.kitching.view.fragment.schedule.ScheduleFragmentDirections
import com.kitching.view.model.ScheduleViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class ScheduleFixAdapter(
    private val lifecycleCoroutineScope: LifecycleCoroutineScope,
    private val currentDate: String
) : ListAdapter<ScheduleDTO, ScheduleViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScheduleViewHolder {
        val binding =
            ItemScheduleListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        navController = Navigation.findNavController(parent)
        return ScheduleViewHolder(binding)
    }

    private val viewModel = ScheduleViewModel.instance

    private var navController: NavController? = null

    override fun onBindViewHolder(
        holder: ScheduleViewHolder,
        position: Int
    ) {
        holder.bindScheduleFix(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ScheduleDTO>() {
            override fun areItemsTheSame(
                oldItem: ScheduleDTO,
                newItem: ScheduleDTO
            ): Boolean {
                return oldItem.scheduleId == newItem.scheduleId
            }

            override fun areContentsTheSame(
                oldItem: ScheduleDTO,
                newItem: ScheduleDTO
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ScheduleViewHolder(val binding: ItemScheduleListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindScheduleFix(schedule: ScheduleDTO) {
            with(binding) {
                scheduleNameTV.text = schedule.userName
                scheduleTimeTV.text = schedule.scheduleTimeName

                rejectBtn.clicks().throttleFirst().onEach {
                    val action =
                        ScheduleFragmentDirections.actionScheduleFragmentToScheduleDeleteDialog(
                            currentDate,
                            schedule.scheduleId
                        )
                    navController?.navigate(action)
                }.launchIn(lifecycleCoroutineScope)
            }
        }
    }
}