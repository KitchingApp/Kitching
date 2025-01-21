package com.kitching.domain.repository

import com.kitching.data.dto.OrderCategoryDTO
import com.kitching.data.dto.OrderDTO
import com.kitching.data.firebase.FirebaseResult
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getOrderList(categoryId: String): Flow<FirebaseResult<List<OrderDTO>>>

    fun createOrder(categoryId: String, orderName: String): Flow<FirebaseResult<Boolean>>

    fun deleteOrder(orderId: String): Flow<FirebaseResult<Boolean>>

    fun updateOrder(orderId: String, orderName: String): Flow<FirebaseResult<Boolean>>
}