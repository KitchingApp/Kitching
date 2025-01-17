package com.kitching.domain.datasource

import com.kitching.domain.entities.Order
import com.kitching.domain.entities.OrderCategory

interface OrderDataSource {
    suspend fun getOrderCategory(teamId: String): Result<List<OrderCategory>>

    suspend fun createOrderCategory(teamId: String, categoryName: String, color: String): Boolean

    suspend fun deleteOrderCategory(categoryId: String): Boolean

    suspend fun updateOrderCategory(categoryId: String, categoryName: String, color: String): Boolean

    suspend fun getOrderList(categoryId: String): Result<List<Order>>

    suspend fun createOrder(categoryId: String, name: String): Boolean

    suspend fun updateOrder(orderId: String, name: String): Boolean

    suspend fun deleteOrder(orderId: String): Boolean
}