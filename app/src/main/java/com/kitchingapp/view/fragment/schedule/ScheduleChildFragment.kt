package com.kitchingapp.view.fragment.schedule

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.lifecycle.lifecycleScope
import com.kitchingapp.R
import com.kitchingapp.adapter.ScheduleApplyAdapter
import com.kitchingapp.adapter.ScheduleFixAdapter
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.ChildfragmentScheduleDepartmentBinding
import com.kitchingapp.domain.entities.Schedule
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class ScheduleChildFragment: BaseFragment<ChildfragmentScheduleDepartmentBinding>(
    ChildfragmentScheduleDepartmentBinding::inflate){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val allSchedules = RestaurantGenerator.restaurantList()
//            .flatMap { it.schedules }
//        val fixSchedules = allSchedules.filter { it.isFix }
//        val applySchedules = allSchedules.filter { !it.isFix }

        with(binding) {
//            val scheduleDepartmentPeopleText = getString(R.string.scheduleDepartmentPeople, fixSchedules.size)
//            scheduleDepartmentPeople.text = scheduleDepartmentPeopleText

            with(confirmedScheduleRecyclerView) {
                setRvLayout(this)

                val fixAdapter = ScheduleFixAdapter()

//                fixAdapter.submitList(fixSchedules)

                this.adapter = fixAdapter
            }

            with(appliedScheduleRecyclerView) {
                setRvLayout(this)

                val applyAdapter = ScheduleApplyAdapter()

//                applyAdapter.submitList(applySchedules)

                this.adapter = applyAdapter
            }

            with(createScheduleBtn) {
                clicks().onEach {
                    val dialog = ScheduleCreateDialog()
                    dialog.show(activity?.supportFragmentManager!!, "dialog")

                }.launchIn(lifecycleScope)
            }
        }
    }
}