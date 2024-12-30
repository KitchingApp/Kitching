package com.kitching.view.fragment.schedule

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
import com.kitching.adapter.ScheduleApplyAdapter
import com.kitching.common.BaseFragment
import com.kitching.databinding.FragmentScheduleBinding
import com.kitching.adapter.ScheduleFixAdapter
import com.kitching.common.throttleFirst
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.data.firebase.FirebaseResult
import com.kitching.view.model.ScheduleViewModel
import com.kitching.view.model.factory.viewModelFactory
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
                    PreferencesDataSource(requireContext()).teamId.collectLatest { teamId ->
                        if (teamId != null) {
                            viewModel.getDepartments(teamId)
                            viewModel.getSchedules(teamId, LocalDate.now().toString())
                        }
                    }
                    viewModel.schedules.collectLatest { schedules ->
                        when(schedules) {
                            is FirebaseResult.Success -> {
                                fixAdapter.submitList(schedules.data.filter { it.isFix })
                                applyAdapter.submitList(schedules.data.filter { !it.isFix })
                            }
                            is FirebaseResult.Loading -> TODO("로딩 처리")
                            is FirebaseResult.Failure -> TODO("예외 처리")
                            is FirebaseResult.DummyConstructor -> TODO("더미 생성")
                        }

                    }
                    viewModel.departments.collectLatest { departments ->
                        with(binding.departmentSelectDropdown) {
                            when(departments) {
                                is FirebaseResult.Success -> {
                                    if (departments.data.isNotEmpty()) {
                                        setText("부서", false)
                                        setSimpleItems(departments.data.map { it.departmentName }.toTypedArray())
                                    }
                                }
                                is FirebaseResult.Loading -> TODO("로딩 처리")
                                is FirebaseResult.Failure -> TODO("예외 처리")
                                is FirebaseResult.DummyConstructor -> TODO("더미 생성")
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
                            val selectDate =
                                LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
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
                    viewModel.schedules.collectLatest { filteredSchedule ->
                        val filteredSchedules = when (filteredSchedule) {
                            is FirebaseResult.Success -> {
                                    filteredSchedule.data.filter { it.departmentName == text.toString() }
                            }
                            is FirebaseResult.Loading -> TODO("로딩 처리")
                            is FirebaseResult.Failure -> TODO("예외 처리")
                            is FirebaseResult.DummyConstructor -> TODO("더미 생성")
                        }
                        fixAdapter.submitList(filteredSchedules.filter { it.isFix })
                        applyAdapter.submitList(filteredSchedules.filter { !it.isFix })
                    }
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