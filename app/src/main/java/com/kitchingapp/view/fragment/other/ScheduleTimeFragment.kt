package com.kitchingapp.view.fragment.other

import RestaurantGenerator
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kitchingapp.adapter.ScheduleTimeAdapter
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.FragmentScheduleTimeBinding

class ScheduleTimeFragment : BaseFragment<FragmentScheduleTimeBinding>(FragmentScheduleTimeBinding::inflate) {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val allScheduleTime = RestaurantGenerator.restaurantList().flatMap { it.scheduleTimes }

        with(binding.scheduleTimeRV) {
            setRvLayout(this)

            val scheduleTimeAdapter = ScheduleTimeAdapter()
//            scheduleTimeAdapter.submitList(allScheduleTime)
            this.adapter = scheduleTimeAdapter

        }
    }
}