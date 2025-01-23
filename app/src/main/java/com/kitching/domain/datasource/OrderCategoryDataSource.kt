package com.kitching.domain.datasource

import com.kitching.domain.entities.Order
import com.kitching.domain.entities.OrderCategory

interface OrderCategoryDataSource {
    suspend fun getOrderCategories(teamId: String): List<OrderCategory>

    suspend fun createOrderCategory(teamId: String, categoryName: String, color: String): Boolean

    suspend fun deleteOrderCategory(categoryId: String): Boolean

    suspend fun updateOrderCategory(categoryId: String, categoryName: String, color: String): Boolean
}