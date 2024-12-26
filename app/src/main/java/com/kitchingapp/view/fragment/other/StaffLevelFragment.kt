package com.kitchingapp.view.fragment.other

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.kitchingapp.adapter.StaffLevelAdapter
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.FragmentStafflevelBinding
import com.kitchingapp.domain.entities.Department
import com.kitchingapp.domain.entities.StaffLevel
import com.kitchingapp.view.model.StaffLevelViewModel
import com.kitchingapp.view.model.factory.staffLevelViewModelFactory
import kotlinx.coroutines.launch

class StaffLevelFragment : BaseFragment<FragmentStafflevelBinding>(FragmentStafflevelBinding::inflate) {
    private lateinit var navController: NavController

    private val args: StaffLevelFragmentArgs by navArgs()

    private val viewModel by viewModels<StaffLevelViewModel> {
        staffLevelViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val staffLevelAdapter = StaffLevelAdapter(viewLifecycleOwner)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.getStaffLevels(args.departmentId)
                    viewModel.staffLevels.collect {
                        staffLevelAdapter.submitList(it)
                    }
                }
            }
        }

        with(binding.staffLevelRV) {
            setRvLayout(this)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = staffLevelAdapter
        }
    }
}