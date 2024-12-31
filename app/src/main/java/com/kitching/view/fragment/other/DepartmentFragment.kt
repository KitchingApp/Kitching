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
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.data.firebase.FirebaseResult
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
                    teamId = PreferencesDataSource(requireContext()).getTeamId() ?: ""
                    viewModel.departments.collectLatest {
                        when(it) {
                            is FirebaseResult.Success -> departmentAdapter.submitList(it.data)
                            is FirebaseResult.Loading -> {} // TODO("로딩 처리)
                            is FirebaseResult.Failure -> {} // TODO("예외 처리")
                            is FirebaseResult.DummyConstructor -> {} // TODO("더미 생성")
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