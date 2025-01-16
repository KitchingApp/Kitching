package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.common.firebaseFlowHandler
import com.kitching.data.dto.OrderCategoryDTO
import com.kitching.data.dto.OrderDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OrderViewModel(private val repository: OrderRepository = OrderRepository()) : ViewModel() {

    /** OrderCategory */
    private val _orderCategory = MutableStateFlow<FirebaseResult<MutableList<OrderCategoryDTO>>>(FirebaseResult.Loading)
    val orderCategory get() = _orderCategory.asStateFlow()

    fun getOrderCategory(teamId: String) {
        firebaseFlowHandler(_orderCategory) {
            repository.getOrderCategory(teamId)
        }
    }

    private val _createOrderCategoryResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val createOrderCategoryResult get() = _createOrderCategoryResult.asStateFlow()

    fun createOrderCategory(teamId: String, categoryName: String, color: String) {
        firebaseFlowHandler(_createOrderCategoryResult) {
            repository.createOrderCategory(teamId, categoryName, color)
        }
    }

    private val _orderList = MutableStateFlow<FirebaseResult<MutableList<OrderDTO>>>(FirebaseResult.Loading)
    val orderList get() = _orderList.asStateFlow()

    fun getOrderList(categoryId: String) {
        firebaseFlowHandler(_orderList) {
            repository.getOrderList(categoryId)
        }
    }

    private val _deleteOrderCategoryResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val deleteOrderCategoryResult get() = _deleteOrderCategoryResult.asStateFlow()

    fun deleteOrderCategory(categoryId: String) {
        firebaseFlowHandler(_deleteOrderCategoryResult) {
            repository.deleteOrderCategory(categoryId)
        }
    }

    private val _updateOrderCategoryResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val updateOrderCategoryResult get() = _updateOrderCategoryResult.asStateFlow()

    fun updateOrderCategory(categoryId: String, categoryName: String, color: String) {
        firebaseFlowHandler(_updateOrderCategoryResult) {
            repository.updateOrderCategory(categoryId, categoryName, color)
        }

    }
}