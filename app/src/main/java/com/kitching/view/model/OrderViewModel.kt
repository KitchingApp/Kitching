package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.data.dto.OrderCategoryDTO
import com.kitching.data.dto.OrderDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.OrderCategoryRepositoryImpl
import com.kitching.data.repository.OrderRepositoryImpl
import com.kitching.domain.repository.OrderCategoryRepository
import com.kitching.domain.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OrderViewModel(
    private val orderCategoryRepository: OrderCategoryRepository = OrderCategoryRepositoryImpl(),
    private val orderRepository: OrderRepository = OrderRepositoryImpl()
) : ViewModel() {

    /** OrderCategory */
    private val _orderCategory = MutableStateFlow<FirebaseResult<List<OrderCategoryDTO>>>(FirebaseResult.Loading)
    val orderCategory get() = _orderCategory.asStateFlow()

    fun getOrderCategory(teamId: String) {
        viewModelScope.launch {
            orderCategoryRepository.getOrderCategory(teamId).collectLatest {
                _orderCategory.value = it
            }
        }
    }

    private val _orderCategoryResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val orderCategoryResult get() = _orderCategoryResult.asStateFlow()

    fun createOrderCategory(teamId: String, categoryName: String, color: String) {
        viewModelScope.launch {
            orderCategoryRepository.createOrderCategory(teamId, categoryName, color).collectLatest {
                _orderCategoryResult.value = it
            }
        }
    }

    fun deleteOrderCategory(categoryId: String) {
        viewModelScope.launch {
            orderCategoryRepository.deleteOrderCategory(categoryId).collectLatest {
                _orderCategoryResult.value = it
            }
        }
    }

    fun updateOrderCategory(categoryId: String, categoryName: String, color: String) {
        viewModelScope.launch {
            orderCategoryRepository.updateOrderCategory(categoryId, categoryName, color).collectLatest {
                _orderCategoryResult.value = it
            }
        }
    }

    private val _orderList = MutableStateFlow<FirebaseResult<List<OrderDTO>>>(FirebaseResult.Loading)
    val orderList get() = _orderList.asStateFlow()

    fun getOrderList(categoryId: String) {
        viewModelScope.launch {
            orderRepository.getOrderList(categoryId).collectLatest {
                _orderList.value = it
            }
        }
    }

    private var _orderResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val orderResult get() = _orderResult.asStateFlow()

    fun createOrder(categoryId: String, orderName: String) {
        viewModelScope.launch {
            orderRepository.createOrder(categoryId, orderName).collectLatest {
                _orderResult.value = it
            }
        }
    }

    fun deleteOrder(orderId: String) {
        viewModelScope.launch {
            orderRepository.deleteOrder(orderId).collectLatest {
                _orderResult.value = it
            }
        }
    }

    fun updateOrder(orderId: String, orderName: String) {
        viewModelScope.launch {
            orderRepository.updateOrder(orderId, orderName).collectLatest {
                _orderResult.value = it
            }
        }
    }
}
