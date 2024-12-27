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
import com.kitchingapp.adapter.ScheduleApplyAdapter
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.FragmentScheduleBinding
import com.kitchingapp.adapter.ScheduleFixAdapter
import com.kitchingapp.common.throttleFirst
import com.kitchingapp.data.database.repository.LocalRepository
import com.kitchingapp.data.database.usecase.LocalType
import com.kitchingapp.data.database.usecase.LocalTypeUseCase
import com.kitchingapp.view.model.ScheduleViewModel
import com.kitchingapp.view.model.factory.viewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks
import ru.ldralighieri.corbind.widget.itemClickEvents
import java.time.LocalDate

class ScheduleFragment() : BaseFragment<FragmentScheduleBinding>(FragmentScheduleBinding::inflate) {
    private lateinit var navController: NavController

    private val viewModel by viewModels<ScheduleViewModel> {
        viewModelFactory
    }

    private val localRepository: LocalRepository by lazy {
        LocalTypeUseCase(requireContext()).selectLocalType(LocalType.DATASTORE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    private val fixAdapter = ScheduleFixAdapter()
    private val applyAdapter = ScheduleApplyAdapter()

    private lateinit var teamId: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    localRepository.teamId.collectLatest {
                        if(it != null) {
                            teamId = it
                            viewModel.getDepartments(teamId)
                            viewModel.getSchedules(teamId, LocalDate.now().toString())
                        }
                    }
                }

                launch {
                    viewModel.schedules.collectLatest { schedules ->
                        fixAdapter.submitList(schedules.filter { it.isFix })
                        applyAdapter.submitList(schedules.filter { !it.isFix })
                    }
                }

                launch {
                    viewModel.departments.collectLatest { departments ->
                        with(binding.departmentSelectDropdown) {
                            if(departments.isNotEmpty()) {
                                setText("부서", false)
                                setSimpleItems(departments.map { it.departmentName }.toTypedArray())
                            }
                        }
                    }
                }
            }
        }

        with(binding) {
            var currentDate = LocalDate.now()

            with(prevDateBtn) {
                clicks().throttleFirst().onEach {
                    val prevDate = currentDate.minusDays(1)
                    scheduleDateBtn.text = prevDate.toString()
                    currentDate = prevDate
                    viewModel.getSchedules(teamId, currentDate.toString())
                }.launchIn(lifecycleScope)
            }

            with(scheduleDateBtn) {
                text = LocalDate.now().toString()

                clicks().throttleFirst().onEach {
                    val year = currentDate.year
                    val month = currentDate.monthValue - 1
                    val day = currentDate.dayOfMonth

                    context.let {
                        DatePickerDialog(it, { _, selectedYear, selectedMonth, selectedDay ->
                            val selectDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                            text = selectDate.toString()
                            currentDate = selectDate
                            viewModel.getSchedules(teamId, currentDate.toString())
                        }, year, month, day).show()
                    }
                }.launchIn(lifecycleScope)
            }

            with(nextDateBtn) {
                clicks().throttleFirst().onEach {
                    val nextDate = currentDate.plusDays(1)
                    scheduleDateBtn.text = nextDate.toString()
                    currentDate = nextDate
                    viewModel.getSchedules(teamId, currentDate.toString())
                }.launchIn(lifecycleScope)
            }

            with(departmentSelectDropdown) {
                itemClickEvents().throttleFirst().onEach {
                    val filteredSchedules = viewModel.schedules.value.filter { it.departmentName == text.toString() }
                    fixAdapter.submitList(filteredSchedules.filter { it.isFix })
                    applyAdapter.submitList(filteredSchedules.filter { !it.isFix })
                }.launchIn(lifecycleScope)
            }

            with(confirmedScheduleRecyclerView) {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = fixAdapter
            }

            with(appliedScheduleRecyclerView) {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = applyAdapter
            }
        }
    }
}