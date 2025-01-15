package com.kitching.view.fragment.other.department

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kitching.adapter.DepartmentAdapter
import com.kitching.common.BaseFragment
import com.kitching.common.firebaseResultHandler
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.databinding.FragmentDepartmentBinding
import com.kitching.view.model.factory.FactoryDepartmentViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DepartmentFragment :
    BaseFragment<FragmentDepartmentBinding>(FragmentDepartmentBinding::inflate) {
    private lateinit var navController: NavController

    private val viewModel = FactoryDepartmentViewModel.fetchDepartmentViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lateinit var teamId: String
        val departmentAdapter = DepartmentAdapter(requireContext(), viewLifecycleOwner, navController)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                teamId = PreferencesDataSource(requireContext()).getTeamId() ?: ""
                viewModel.getDepartments(teamId)
                viewModel.departments.collectLatest {
                    firebaseResultHandler(it) { data ->
                        departmentAdapter.submitList(data)
                    }
                }
            }
        }

        with(binding.departmentRV) {
            setRvLayout(this)
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = departmentAdapter
        }

        setPlusActionBtn {
            navController.navigate(DepartmentFragmentDirections.actionDepartmentFragmentToDepartmentCreateDialog())
        }
    }
}