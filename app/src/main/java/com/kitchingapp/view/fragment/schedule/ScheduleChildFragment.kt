package com.kitchingapp.view.fragment.schedule

import android.os.Bundle
import android.view.View
import com.kitchingapp.adapter.ScheduleFixAdapter
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.ChildfragmentScheduleDepartmentBinding
import com.kitchingapp.domain.entities.Schedule

class ScheduleChildFragment: BaseFragment<ChildfragmentScheduleDepartmentBinding>(
    ChildfragmentScheduleDepartmentBinding::inflate){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.confirmedScheduleRecyclerView) {
            setRvLayout(this@with)

            val adapter = ScheduleFixAdapter()

            val allSchedules = RestaurantGenerator.restaurantList()
                .flatMap { it.schedules }
                .filter { it.isFix }

            adapter.submitList(allSchedules)

            this.adapter = adapter
        }
    }
}