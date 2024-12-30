package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.data.dto.OrderCategoryDTO
import com.kitching.data.dto.OrderDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OrderViewModel(private val repository: OrderRepository = OrderRepository()) : ViewModel() {

    private val _orderCategory = MutableStateFlow<FirebaseResult<MutableList<OrderCategoryDTO>>>(FirebaseResult.Loading)
    val orderCategory get() = _orderCategory.asStateFlow()

    fun getOrderCategory(teamId: String) {
        viewModelScope.launch {
            repository.getOrderCategory(teamId).collectLatest {
                _orderCategory.value = it
            }
        }
    }

    private val _orderList = MutableStateFlow<FirebaseResult<MutableList<OrderDTO>>>(FirebaseResult.Loading)
    val orderList get() = _orderList.asStateFlow()

    fun getOrderList(categoryId: String) {
        viewModelScope.launch {
            repository.getOrderList(categoryId).collectLatest {
                _orderList.value = it
            }
        }
    }
}