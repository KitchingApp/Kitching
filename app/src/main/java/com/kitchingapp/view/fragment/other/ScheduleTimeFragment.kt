package com.kitchingapp.view.fragment.other

import RestaurantGenerator
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kitchingapp.adapter.ScheduleTimeAdapter
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.data.database.repository.LocalRepository
import com.kitchingapp.data.database.repository.RemoteRepository
import com.kitchingapp.data.database.usecase.LocalType
import com.kitchingapp.data.database.usecase.LocalTypeUseCase
import com.kitchingapp.data.database.usecase.RemoteType
import com.kitchingapp.data.database.usecase.RemoteTypeUseCase
import com.kitchingapp.databinding.FragmentScheduleTimeBinding

class ScheduleTimeFragment : BaseFragment<FragmentScheduleTimeBinding>(FragmentScheduleTimeBinding::inflate) {
    private lateinit var navController: NavController

    private val localRepository: LocalRepository by lazy {
        LocalTypeUseCase(requireContext()).selectLocalType(LocalType.DATASTORE)
    }

    private val remoteRepository: RemoteRepository by lazy {
        RemoteTypeUseCase.selectRemoteType(RemoteType.FIREBASE)
    }

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