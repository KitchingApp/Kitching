package com.kitchingapp.view.fragment.schedule

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.FragmentScheduleBinding
import com.kitchingapp.R
import com.kitchingapp.domain.entities.User
import com.kitchingapp.view.model.ScheduleViewModel
import com.kitchingapp.view.model.factory.scheduleViewModelFactory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import java.time.LocalDate

class ScheduleFragment : BaseFragment<FragmentScheduleBinding>(FragmentScheduleBinding::inflate) {
    private lateinit var navController: NavController

    inner class DepartmentMock(val name: String, val color: Int, val members: List<User>)

    private val viewModel by viewModels<ScheduleViewModel> {
        scheduleViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            var currentDate = LocalDate.now()

            with(departmentSelectDropdown) {
                val departmentsMockData = arrayOf<String>("홀", "주방")
                departmentsMockData.forEach {
                    setText(departmentsMockData[0]) // 기본값 설정
                    setSimpleItems(departmentsMockData)
                }
            }

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