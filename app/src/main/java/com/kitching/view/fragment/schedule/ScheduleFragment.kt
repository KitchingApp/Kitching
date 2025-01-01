package com.kitching.view.fragment.schedule

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kitching.adapter.ScheduleApplyAdapter
import com.kitching.common.BaseFragment
import com.kitching.databinding.FragmentScheduleBinding
import com.kitching.adapter.ScheduleFixAdapter
import com.kitching.common.throttleClicks
import com.kitching.common.throttleFirst
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.data.dto.ScheduleDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.view.model.ScheduleViewModel
import com.kitching.view.model.factory.viewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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
                setAdapters()
                setTeamId()

                launch {
                    collectDepartments()
                }
                launch {
                    collectSchedules(null)
                }

                with(binding.departmentSelectDropdown) {
                    itemClickEvents().throttleFirst().onEach {
                        collectSchedules(text.toString())
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }
            }
        }
        setDateBtn(viewLifecycleOwner)
    }


    /** teamId 세팅 */
    private suspend fun setTeamId() {
        teamId = PreferencesDataSource(requireContext()).getTeamId() ?: ""
        viewModel.getDepartments(teamId)
        viewModel.getSchedules(teamId, LocalDate.now().toString())
    }

    private suspend fun collectDepartments() {
        viewModel.departments.collectLatest { departments ->
            when (departments) {
                is FirebaseResult.Success -> {
                    if (departments.data.isNotEmpty()) {
                        with(binding.departmentSelectDropdown) {
                            setText("부서", false)
                            setSimpleItems(departments.data.map { it.departmentName }
                                .toTypedArray())
                        }
                    }
                    setAdapters()
                }

                is FirebaseResult.Loading -> {} // TODO("로딩 처리)
                is FirebaseResult.Failure -> {} // TODO("예외 처리")
                is FirebaseResult.DummyConstructor -> {} // TODO("더미 생성")
            }
        }
    }

    private suspend fun collectSchedules(selectedDepartment: String?) {
        viewModel.schedules.collectLatest { schedules ->
            when (schedules) {
                is FirebaseResult.Success -> {
                    val filteredSchedules = if (selectedDepartment.isNullOrBlank()) {
                        schedules.data
                    } else {
                        schedules.data.filter { it.departmentName == selectedDepartment }
                    }
                    submitSchedules(filteredSchedules)
                }

                is FirebaseResult.Loading -> {} // TODO("로딩 처리)
                is FirebaseResult.Failure -> {} // TODO("예외 처리")
                is FirebaseResult.DummyConstructor -> {} // TODO("더미 생성")
            }
        }
    }

    /** 리사이클러뷰 어댑터에 스케줄 분리 후 전달 */
    private fun submitSchedules(schedules: List<ScheduleDTO>) {
        fixAdapter.submitList(schedules.filter { it.isFix })
        applyAdapter.submitList(schedules.filter { !it.isFix })
    }

    /** 리사이클러뷰 어댑터 세팅 */
    private fun setAdapters() {
        with(binding) {
            confirmedScheduleRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = fixAdapter
            }
            appliedScheduleRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = applyAdapter
            }
        }
    }

    /** 날짜 버튼 세팅 */
    private fun setDateBtn(lifecycleOwner: LifecycleOwner) {
        var currentDate = LocalDate.now()
        with(binding) {
            scheduleDateBtn.text = currentDate.toString()

            prevDateBtn.throttleClicks(lifecycleOwner) {
                currentDate = currentDate.minusDays(1)
                setDate(currentDate)
            }

            scheduleDateBtn.throttleClicks(lifecycleOwner) {
                val year = currentDate.year
                val month = currentDate.monthValue - 1
                val day = currentDate.dayOfMonth

                DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                    currentDate =
                        LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                    setDate(currentDate)
                }, year, month, day).show()
            }

            nextDateBtn.throttleClicks(lifecycleOwner) {
                currentDate = currentDate.plusDays(1)
                setDate(currentDate)
            }
        }
    }

    /** 날짜 버튼을 바꾸고 스케줄 뷰모델 업데이트 */
    private fun setDate(targetDate: LocalDate) {
        binding.scheduleDateBtn.text = targetDate.toString()
        viewModel.getSchedules(teamId, targetDate.toString())
    }
}