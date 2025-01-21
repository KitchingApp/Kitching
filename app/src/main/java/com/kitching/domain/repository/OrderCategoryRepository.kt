package com.kitching.domain.repository

import com.kitching.data.dto.OrderCategoryDTO
import com.kitching.data.dto.OrderDTO
import com.kitching.data.firebase.FirebaseResult
import kotlinx.coroutines.flow.Flow

interface OrderCategoryRepository {
    fun getOrderCategory(teamId: String): Flow<FirebaseResult<List<OrderCategoryDTO>>>

    fun createOrderCategory(teamId: String, categoryName: String, color: String): Flow<FirebaseResult<Boolean>>

    fun deleteOrderCategory(categoryId: String): Flow<FirebaseResult<Boolean>>

    fun updateOrderCategory(categoryId: String, categoryName: String, color: String): Flow<FirebaseResult<Boolean>>
}