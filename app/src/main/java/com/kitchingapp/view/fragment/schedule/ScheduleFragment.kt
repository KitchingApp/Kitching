package com.kitchingapp.view.fragment.schedule

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.FragmentScheduleBinding
import com.kitchingapp.adapter.ScheduleFixAdapter
import com.kitchingapp.domain.entities.User
import com.kitchingapp.view.model.ScheduleViewModel
import com.kitchingapp.view.model.factory.scheduleViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks
import java.time.LocalDate

class ScheduleFragment() : BaseFragment<FragmentScheduleBinding>(FragmentScheduleBinding::inflate) {
    private lateinit var navController: NavController
    private lateinit var department: String

    inner class DepartmentMock(val name: String, val color: Int, val members: List<User>)

    private val viewModel by viewModels<ScheduleViewModel> {
        scheduleViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSchedules("3uM01g5GSz8lC49JA6vq", LocalDate.now().toString())
        viewModel.getDepartments("3uM01g5GSz8lC49JA6vq")

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                with(viewModel) {
                    departments.collectLatest { departments ->
                        departments.forEach { _ ->
                            with(binding.departmentSelectDropdown) {
                                setText(departments.getOrNull(0)?.departmentName) // 초기값 설정.
                                setSimpleItems(departments.map { it.departmentName }.toTypedArray())
                            }
                        }
                    }
                    schedules.collectLatest { schedules ->
                        schedules.forEach { _ ->
                            with(binding.confirmedScheduleRecyclerView) {
                                setRvLayout(this)
                                layoutManager = LinearLayoutManager(requireContext())
                                val adapter = ScheduleFixAdapter()
//                                adapter.submitList(viewModel.schedulesByDepartment())
                            }
                        }
                    }
                }
            }
        }

        with(binding) {
            var currentDate = LocalDate.now()

            with(prevDateBtn) {
                clicks().onEach {
                    val prevDate = currentDate.minusDays(1)
                    scheduleDateBtn.text = prevDate.toString()
                    currentDate = prevDate
                }.launchIn(lifecycleScope)
            }

            with(scheduleDateBtn) {
                text = LocalDate.now().toString()

                clicks().onEach {
                    val year = currentDate.year
                    val month = currentDate.monthValue - 1
                    val day = currentDate.dayOfMonth

                    context.let {
                        DatePickerDialog(it, { _, selectedYear, selectedMonth, selectedDay ->
                            currentDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                            text = currentDate.toString()
                        }, year, month, day).show()
                    }
                }.launchIn(lifecycleScope)
            }

            with(nextDateBtn) {
                clicks().onEach {
                    val nextDate = currentDate.plusDays(1)
                    scheduleDateBtn.text = nextDate.toString()
                    currentDate = nextDate

                }.launchIn(lifecycleScope)
            }
        }


    }
}