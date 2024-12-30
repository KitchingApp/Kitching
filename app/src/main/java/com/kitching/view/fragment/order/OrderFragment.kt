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
import com.kitching.data.dto.OrderCategoryDTO
import com.kitching.data.firebase.FirebaseResult
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.orderCategory.collectLatest {
                    when(it) {
                        is FirebaseResult.Success -> notifyOrderCategory(it.data)
                        is FirebaseResult.Loading -> TODO("로딩 처리")
                        is FirebaseResult.Failure -> TODO("예외 처리")
                        is FirebaseResult.DummyConstructor -> TODO("더미 생성")
                    }
                }
            }
        }
        viewModel.getOrderCategory(teamId = "3uM01g5GSz8lC49JA6vq")
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