package com.kitchingapp.view.fragment.other

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kitchingapp.adapter.ScheduleTimeAdapter
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.data.database.repository.LocalRepository
import com.kitchingapp.data.database.usecase.LocalType
import com.kitchingapp.data.database.usecase.LocalTypeUseCase
import com.kitchingapp.databinding.FragmentScheduleTimeBinding
import com.kitchingapp.view.model.ScheduleTimeViewModel
import com.kitchingapp.view.model.factory.viewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ScheduleTimeFragment : BaseFragment<FragmentScheduleTimeBinding>(FragmentScheduleTimeBinding::inflate) {
    private lateinit var navController: NavController

    private val viewModel by viewModels<ScheduleTimeViewModel> {
        viewModelFactory
    }

    private val localRepository: LocalRepository by lazy {
        LocalTypeUseCase(requireContext()).selectLocalType(LocalType.DATASTORE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lateinit var teamId: String
        val scheduleTimeAdapter = ScheduleTimeAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    localRepository.teamId.collectLatest {
                        if (it != null) {
                            teamId = it
                            viewModel.getScheduleTimes(teamId)
                        }
                    }
                }

                launch {
                    viewModel.scheduleTime.collectLatest {
                        scheduleTimeAdapter.submitList(it)
                    }
                }
            }
        }

        with(binding.scheduleTimeRV) {
            setRvLayout(this)
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = scheduleTimeAdapter
        }
    }
}