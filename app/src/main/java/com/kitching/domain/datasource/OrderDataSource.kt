package com.kitching.domain.datasource

import com.kitching.domain.entities.Order
import com.kitching.domain.entities.OrderCategory

interface OrderDataSource {
    suspend fun getOrderList(categoryId: String): List<Order>

    suspend fun createOrder(categoryId: String, name: String): Boolean

    suspend fun updateOrder(orderId: String, name: String): Boolean

    suspend fun deleteOrder(orderId: String): Boolean
}