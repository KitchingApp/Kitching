package com.kitching.view.fragment.prep

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kitching.adapter.PrepCategoryAdapter
import com.kitching.common.BaseFragment
import com.kitching.common.util.throttleFirst
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.data.firebase.FirebaseResult
import com.kitching.databinding.FragmentPrepBinding
import com.kitching.view.model.PrepViewModel
import com.kitching.view.model.factory.viewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class PrepCategoryFragment : BaseFragment<FragmentPrepBinding>(FragmentPrepBinding::inflate) {
    private lateinit var navController: NavController

    private val viewModel = PrepViewModel.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prepCategoryAdapter = PrepCategoryAdapter(viewLifecycleOwner)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    val teamId = PreferencesDataSource(requireContext()).getTeamId() ?: ""
                    viewModel.getPrepCategory(teamId)
                    viewModel.prepCategory.collectLatest {
                        when(it) {
                            is FirebaseResult.Success -> prepCategoryAdapter.submitList(it.data)
                            is FirebaseResult.Loading -> {} // TODO("로딩 처리)
                            is FirebaseResult.Failure -> {} // TODO("예외 처리")
                            is FirebaseResult.DummyConstructor -> {} // TODO("더미 생성")
                        }
                    }
                }
            }
        }

        with(binding) {
            with(todoCategoryRV) {
                setRvLayout(this)
                this.adapter = prepCategoryAdapter
            }
        }

        setActionBtn {
            val action = PrepCategoryFragmentDirections.actionPrepFragmentToPrepCategoryCreateDialog()
            navController.navigate(action)
        }
    }
}