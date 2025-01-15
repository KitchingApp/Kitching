package com.kitching.view.fragment.schedule

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kitching.R
import com.kitching.adapter.ScheduleApplyAdapter
import com.kitching.common.BaseFragment
import com.kitching.databinding.FragmentScheduleBinding
import com.kitching.adapter.ScheduleFixAdapter
import com.kitching.common.firebaseResultHandler
import com.kitching.common.util.throttleClicks
import com.kitching.common.util.throttleFirst
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.data.firebase.FirebaseResult
import com.kitching.view.model.ScheduleViewModel
import com.kitching.view.model.factory.FactoryScheduleViewModel
import com.kitching.view.model.factory.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.widget.itemClickEvents
import java.time.LocalDate

class ScheduleFragment : BaseFragment<FragmentScheduleBinding>(FragmentScheduleBinding::inflate) {
    private lateinit var navController: NavController

    //    private val viewModel = FactoryScheduleViewModel.fetchScheduleViewModel()
    private val viewModel by viewModels<ScheduleViewModel> {
        ViewModelFactory
    }


    private lateinit var teamId: String
    private var currentDate = LocalDate.now()
    private var selectedDepartment: String? = null

    private lateinit var fixAdapter: ScheduleFixAdapter
    private lateinit var applyAdapter: ScheduleApplyAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        fixAdapter = ScheduleFixAdapter(viewLifecycleOwner, currentDate.toString())
        applyAdapter = ScheduleApplyAdapter(this, viewLifecycleOwner, currentDate.toString())

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                with(viewModel) {
                    teamId = PreferencesDataSource(requireContext()).getTeamId() ?: ""
                    getDepartments(teamId)
                    getSchedules(teamId, LocalDate.now().toString())
                }

                launch {
                    collectDepartments()
                }
                launch {
                    collectFixedSchedules()
                }
                launch {
                    collectAppliedSchedules()
                }

                with(binding.departmentSelectDropdown) {
                    itemClickEvents().throttleFirst().onEach {
                        collectFixedSchedules()
                        collectAppliedSchedules()
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }
            }
        }

        setAdapters()
        setDateBtn(viewLifecycleOwner)
        setBottomSheet()
        setPlusActionBtn(
            onClickAddBtn = {
                val action =
                    ScheduleFragmentDirections.actionScheduleFragmentToScheduleCreateDialog(
                        currentDate.toString()
                    )
                navController.navigate(action)
            }
        )
    }

    /** 부서 업데이트 */
    private suspend fun collectDepartments() {
        viewModel.departments.collectLatest { departments ->
            firebaseResultHandler(departments) { data ->
                if (data.isNotEmpty()) {
                    with(binding.departmentSelectDropdown) {
                        setText("부서", false)
                        setSimpleItems(data.map { it.departmentName }
                            .toTypedArray())
                    }
                }
                setAdapters()
            }
        }
    }

    /** 확정 스케줄 업데이트 */
    private suspend fun collectFixedSchedules() {
        viewModel.fixedSchedules.collectLatest { schedules ->
            firebaseResultHandler(schedules) { data ->
                val filteredSchedules = if (selectedDepartment.isNullOrBlank()) {
                    data
                } else {
                    data.filter { it.departmentName == selectedDepartment }
                }
                fixAdapter.submitList(filteredSchedules)
                binding.scheduleDepartmentPeople.text =
                    getString(R.string.scheduleDepartmentPeople, filteredSchedules.size)
            }
        }
    }

    /** 신청 스케줄 업데이트 */
    private suspend fun collectAppliedSchedules() {
        viewModel.appliedSchedules.collectLatest { schedules ->
            firebaseResultHandler(schedules) { data ->
                val filteredSchedules = if (selectedDepartment.isNullOrBlank()) {
                    data
                } else {
                    data.filter { it.departmentName == selectedDepartment }
                }
                applyAdapter.submitList(filteredSchedules)
            }
        }
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

    /** 바텀 시트 세팅 */
    private fun setBottomSheet() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetContainer)

        with(bottomSheetBehavior) {
            state = BottomSheetBehavior.STATE_COLLAPSED
            peekHeight = 120
            isDraggable = true
            isHideable = false
        }
    }
}