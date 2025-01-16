package com.kitching.view.fragment.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kitching.adapter.OrderCategoryAdapter
import com.kitching.common.BaseFragment
import com.kitching.common.firebaseResultHandler
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.data.dto.OrderCategoryDTO
import com.kitching.databinding.FragmentOrderBinding
import com.kitching.view.model.OrderViewModel
import com.kitching.view.model.factory.viewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OrderFragment : BaseFragment<FragmentOrderBinding>(FragmentOrderBinding::inflate){
    private lateinit var navController: NavController
    private val viewModel by viewModels<OrderViewModel> {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        setPlusActionBtn {
            val action = OrderFragmentDirections.actionOrderFragmentToOrderCategoryCreateDialog()
            navController.navigate(action)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            val teamId = PreferencesDataSource(requireContext()).getTeamId().toString()
            viewModel.getOrderCategory(teamId)

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.orderCategory.collectLatest {
                    firebaseResultHandler(it) { data ->
                        notifyOrderCategory(data)
                    }
                }
            }
        }
    }

    private fun notifyOrderCategory(orderCategory: List<OrderCategoryDTO>?) {
        with(binding.orderCategoryRV) {
            setRvLayout(this)

            val categoryAdapter = OrderCategoryAdapter(viewLifecycleOwner, navController)
            categoryAdapter.submitList(orderCategory)
            this.adapter = categoryAdapter
        }
    }
}