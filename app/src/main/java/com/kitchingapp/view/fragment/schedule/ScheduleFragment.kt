package com.kitchingapp.view.fragment.schedule

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.FragmentScheduleBinding
import com.kitchingapp.R
import com.kitchingapp.domain.entities.Department
import com.kitchingapp.domain.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.material.selections
import ru.ldralighieri.corbind.view.clicks
import java.time.LocalDate

class ScheduleFragment : BaseFragment<FragmentScheduleBinding>(FragmentScheduleBinding::inflate) {
    private lateinit var navController: NavController

    inner class DepartmentMock(val name: String, val color: Int, val members: List<User>)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
        childFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, ScheduleChildFragment()).commit()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            var currentDate = LocalDate.now()

            with(scheduleTab) {
                // 동적으로 탭 추가하기
                val departmentsMockData = listOf<String>("홀", "주방")
                departmentsMockData.forEach {
                    addTab(createNewTab(it))
                }
                //
                selections().onEach {
                    // 탭 클릭 시 동작 추가
                }.launchIn(lifecycleScope)
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

    /** 동적으로 탭 생성하기 */
    private fun createNewTab(tabName: String): TabLayout.Tab {
        val newTab = binding.scheduleTab.newTab()
        newTab.text = tabName

        return newTab
    }
}