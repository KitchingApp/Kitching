package com.kitching.view.model.factory

import com.kitching.view.model.OrderViewModel

class FactoryOrderViewModel {
    companion object{
        private lateinit var orderViewModel: OrderViewModel

        fun fetchOrderViewModel(): OrderViewModel {
            if (!Companion::orderViewModel.isInitialized) {
                orderViewModel = OrderViewModel()
            }
            return orderViewModel
        }
    }

}