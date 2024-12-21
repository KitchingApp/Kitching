package com.kitchingapp.view.fragment.order

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kitchingapp.adapter.OrderListAdapter
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.FragmentOrderlistBinding
import com.kitchingapp.domain.entities.Order
import com.kitchingapp.domain.entities.OrderCategory

class OrderListFragment: BaseFragment<FragmentOrderlistBinding>(FragmentOrderlistBinding::inflate) {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val orderListMockData = listOf(
//            Order("돼지고기", OrderCategory("수산물", Color.parseColor("#90CAF9"))),
//            Order("소고기", OrderCategory("육가공", Color.parseColor("#EF9A9A")))
//        )

        with(binding.orderListRV) {
            setRvLayout(this)

            val orderListAdapter = OrderListAdapter(viewLifecycleOwner)
//            orderListAdapter.submitList(orderListMockData)
            this.adapter = orderListAdapter
        }

    }
}