package com.kitching.view.fragment.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kitching.adapter.OrderListAdapter
import com.kitching.common.BaseFragment
import com.kitching.data.dto.OrderDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.databinding.FragmentOrderlistBinding
import com.kitching.view.model.OrderViewModel
import com.kitching.view.model.factory.viewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

class OrderListFragment: BaseFragment<FragmentOrderlistBinding>(FragmentOrderlistBinding::inflate) {
    private lateinit var navController: NavController
    private val viewModel by viewModels<OrderViewModel> {
        viewModelFactory
    }
    private val args: OrderListFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.orderList.collectLatest {
                    when(it) {
                        is FirebaseResult.Success -> notifyOrderList(it.data)
                        is FirebaseResult.Loading -> {} // TODO("로딩 처리)
                        is FirebaseResult.Failure -> {} // TODO("예외 처리")
                        is FirebaseResult.DummyConstructor -> {} // TODO("더미 생성")
                    }
                }
            }
        }
        viewModel.getOrderList(categoryId = args.orderCategoryId)
    }

    private fun notifyOrderList(orderList: List<OrderDTO>) {
        with(binding.orderListRV) {
            setRvLayout(this)

            val orderListAdapter = OrderListAdapter(viewLifecycleOwner)
            orderListAdapter.submitList(orderList)
            this.adapter = orderListAdapter
        }
    }
}