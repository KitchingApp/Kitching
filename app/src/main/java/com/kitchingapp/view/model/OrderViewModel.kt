package com.kitchingapp.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitchingapp.data.database.dto.OrderCategoryDTO
import com.kitchingapp.data.database.dto.OrderDTO
import com.kitchingapp.data.database.repository.RemoteRepository
import com.kitchingapp.data.database.usecase.RemoteType
import com.kitchingapp.data.database.usecase.RemoteTypeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel(private val remoteType: RemoteType) : ViewModel() {
    private val remoteRepository: RemoteRepository by lazy {
        RemoteTypeUseCase.selectRemoteType(remoteType)
    }

    private val _orderCategory = MutableStateFlow<MutableList<OrderCategoryDTO>>(mutableListOf<OrderCategoryDTO>())
    val orderCategory get() = _orderCategory.asStateFlow()

    fun getOrderCategory(teamId: String) {
        viewModelScope.launch {
            _orderCategory.value = remoteRepository.getOrderCategory(teamId)
        }
    }

    private val _orderList = MutableStateFlow<MutableList<OrderDTO>>(mutableListOf<OrderDTO>())
    val orderList get() = _orderList.asStateFlow()

    fun getOrderList(categoryId: String) {
        viewModelScope.launch {
            _orderList.value = remoteRepository.getOrderList(categoryId)
        }
    }
}