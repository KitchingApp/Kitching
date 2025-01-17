package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.data.dto.OrderCategoryDTO
import com.kitching.data.dto.OrderDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.OrderRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel(private val repository: OrderRepositoryImpl = OrderRepositoryImpl()) : ViewModel() {

    /** OrderCategory */
    private val _orderCategory = MutableStateFlow<FirebaseResult<List<OrderCategoryDTO>>>(FirebaseResult.Loading)
    val orderCategory get() = _orderCategory.asStateFlow()

    fun getOrderCategory(teamId: String) {
        viewModelScope.launch {
            repository.getOrderCategory(teamId).collect {
                _orderCategory.value = it
            }
        }
    }

    private val _orderCategoryResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val orderCategoryResult get() = _orderCategoryResult.asStateFlow()

    fun createOrderCategory(teamId: String, categoryName: String, color: String) {
        viewModelScope.launch {
            repository.createOrderCategory(teamId, categoryName, color).collect {
                _orderCategoryResult.value = it
            }
        }
    }

//    private val _deleteOrderCategoryResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
//    val deleteOrderCategoryResult get() = _deleteOrderCategoryResult.asStateFlow()

    fun deleteOrderCategory(categoryId: String) {
        viewModelScope.launch {
            repository.deleteOrderCategory(categoryId).collect {
                _orderCategoryResult.value = it
            }
        }
    }

//    private val _updateOrderCategoryResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
//    val updateOrderCategoryResult get() = _updateOrderCategoryResult.asStateFlow()

    fun updateOrderCategory(categoryId: String, categoryName: String, color: String) {
        viewModelScope.launch {
            repository.updateOrderCategory(categoryId, categoryName, color).collect {
                _orderCategoryResult.value = it
            }
        }
    }

    /** Order */
    private val _orderList = MutableStateFlow<FirebaseResult<List<OrderDTO>>>(FirebaseResult.Loading)
    val orderList get() = _orderList.asStateFlow()

    fun getOrderList(categoryId: String) {
        viewModelScope.launch {
            repository.getOrderList(categoryId).collect {
                _orderList.value = it
            }
        }
    }

    private var _orderResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val orderResult get() = _orderResult.asStateFlow()

    fun createOrder(categoryId: String, orderName: String) {
        viewModelScope.launch {
            repository.createOrder(categoryId, orderName).collect {
                _orderResult.value = it
            }
        }
    }

//    private val _deleteOrderResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
//    val deleteOrderResult get() = _deleteOrderResult.asStateFlow()

    fun deleteOrder(orderId: String) {
        viewModelScope.launch {
            repository.deleteOrder(orderId).collect {
                _orderResult.value = it
            }
        }
    }

//    private val _updateOrderResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
//    val updateOrderResult get() = _updateOrderResult.asStateFlow()

    fun updateOrder(orderId: String, orderName: String) {
        viewModelScope.launch {
            repository.updateOrder(orderId, orderName).collect {
                _orderResult.value = it
            }
        }
    }
}
