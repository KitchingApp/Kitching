package com.kitching.view.fragment.other.department

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.kitching.adapter.StaffLevelAdapter
import com.kitching.common.BaseFragment
import com.kitching.common.firebaseResultHandler
import com.kitching.databinding.FragmentStafflevelBinding
import com.kitching.view.model.DepartmentViewModel
import kotlinx.coroutines.launch

class StaffLevelFragment :
    BaseFragment<FragmentStafflevelBinding>(FragmentStafflevelBinding::inflate) {
    private lateinit var navController: NavController

    private val args: StaffLevelFragmentArgs by navArgs()

    private val viewModel = DepartmentViewModel.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val staffLevelAdapter = StaffLevelAdapter(requireContext(), viewLifecycleOwner, navController)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getStaffLevels(args.departmentId)
                viewModel.staffLevels.collect {
                    firebaseResultHandler(it) { data ->
                        staffLevelAdapter.submitList(data)
                    }
                }
            }
        }

        with(binding.staffLevelRV) {
            setRvLayout(this)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = staffLevelAdapter
        }

        setPlusActionBtn {
            navController.navigate(
                StaffLevelFragmentDirections.actionStaffLevelFragmentToStaffLevelCreateDialog(
                    args.departmentId
                )
            )
        }
    }
}