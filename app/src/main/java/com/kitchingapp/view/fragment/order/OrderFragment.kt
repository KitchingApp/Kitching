package com.kitchingapp.view.fragment.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kitchingapp.adapter.OrderCategoryAdapter
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.data.database.dto.OrderCategoryDTO
import com.kitchingapp.databinding.FragmentOrderBinding
import com.kitchingapp.view.model.OrderViewModel
import com.kitchingapp.view.model.factory.orderViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OrderFragment : BaseFragment<FragmentOrderBinding>(FragmentOrderBinding::inflate){
    private lateinit var navController: NavController
    private val viewModel by viewModels<OrderViewModel> {
        orderViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.orderCategory.collectLatest {
                    notifyOrderCategory(it)
                }
            }
        }
        viewModel.getOrderCategory(teamId = "3uM01g5GSz8lC49JA6vq")
    }

    private fun notifyOrderCategory(orderCategory: List<OrderCategoryDTO>) {
        with(binding.orderCategoryRV) {
            setRvLayout(this)

            val categoryAdapter = OrderCategoryAdapter(viewLifecycleOwner, navController)
            categoryAdapter.submitList(orderCategory)
            this.adapter = categoryAdapter
        }
    }
}