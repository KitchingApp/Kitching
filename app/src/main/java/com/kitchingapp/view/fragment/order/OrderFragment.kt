package com.kitchingapp.view.fragment.order

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kitchingapp.adapter.OrderAdapter
import com.kitchingapp.common.BaseFragment
import com.kitchingapp.databinding.FragmentOrderBinding
import com.kitchingapp.domain.entities.OrderCategory

class OrderFragment : BaseFragment<FragmentOrderBinding>(FragmentOrderBinding::inflate){
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val orderCategoryMockData = listOf(
//            OrderCategory("수산물", Color.parseColor("#90CAF9")),
//            OrderCategory("육가공", Color.parseColor("#EF9A9A"))
//        )

        with(binding.orderCategoryRV) {
            setRvLayout(this)

            val categoryAdapter = OrderAdapter(viewLifecycleOwner, navController)
//            categoryAdapter.submitList(orderCategoryMockData)
            this.adapter = categoryAdapter
        }
    }
}