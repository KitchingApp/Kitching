package com.kitching.view.fragment.prep

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kitching.adapter.PrepCategoryAdapter
import com.kitching.common.BaseFragment
import com.kitching.common.firebaseResultHandler
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.databinding.FragmentPrepBinding
import com.kitching.view.model.factory.FactoryPrepViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PrepCategoryFragment : BaseFragment<FragmentPrepBinding>(FragmentPrepBinding::inflate) {
    private lateinit var navController: NavController

    private val viewModel = FactoryPrepViewModel.fetchPrepViewModel()

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
                        firebaseResultHandler(it) { data ->
                            prepCategoryAdapter.submitList(data)
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

        setPlusActionBtn {
            val action = PrepCategoryFragmentDirections.actionPrepFragmentToPrepCategoryCreateDialog()
            navController.navigate(action)
        }
    }
}