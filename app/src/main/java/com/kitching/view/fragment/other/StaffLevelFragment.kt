package com.kitching.view.fragment.other

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.kitching.adapter.StaffLevelAdapter
import com.kitching.common.BaseFragment
import com.kitching.databinding.FragmentStafflevelBinding
import com.kitching.view.model.StaffLevelViewModel
import com.kitching.view.model.factory.viewModelFactory
import kotlinx.coroutines.launch

class StaffLevelFragment : BaseFragment<FragmentStafflevelBinding>(FragmentStafflevelBinding::inflate) {
    private lateinit var navController: NavController

    private val args: StaffLevelFragmentArgs by navArgs()

    private val viewModel by viewModels<StaffLevelViewModel> {
        viewModelFactory
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