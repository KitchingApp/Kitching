package com.kitching.view.fragment.other

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
import com.kitching.data.database.repository.LocalRepository
import com.kitching.data.database.usecase.LocalType
import com.kitching.data.database.usecase.LocalTypeUseCase
import com.kitching.databinding.FragmentDepartmentBinding
import com.kitching.view.model.DepartmentViewModel
import com.kitching.view.model.factory.viewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DepartmentFragment :
    BaseFragment<FragmentDepartmentBinding>(FragmentDepartmentBinding::inflate) {
    private lateinit var navController: NavController

    private val viewModel by viewModels<DepartmentViewModel> {
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
        val departmentAdapter = DepartmentAdapter(viewLifecycleOwner, navController)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    localRepository.teamId.collectLatest {
                        if (it != null) {
                            teamId = it
                            viewModel.getDepartments(teamId)
                        }
                    }
                }

                launch {
                    viewModel.departments.collectLatest {
                        departmentAdapter.submitList(it)
                    }
                }
            }
        }

        with(binding.departmentRV) {
            setRvLayout(this)
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = departmentAdapter
        }
    }
}