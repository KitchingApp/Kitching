package com.kitching.data.repository

import com.kitching.data.dto.OrderCategoryDTO
import com.kitching.data.dto.OrderDTO
import com.kitching.data.firebase.FireStoreDataSource
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.firebase.fetchFirebaseDataFlow
import kotlinx.coroutines.flow.Flow

class OrderRepository(private val dataSource: FireStoreDataSource = FireStoreDataSource()) {
    suspend fun getOrderCategory(teamId: String): Flow<FirebaseResult<MutableList<OrderCategoryDTO>>> {
        return fetchFirebaseDataFlow(
            fetcher = { dataSource.getOrderCategory(teamId) },
            mapper = {
                OrderCategoryDTO(
                    categoryId = it.id,
                    categoryName = it.name,
                    color = it.color
                )
            }
        )
    }

    suspend fun getOrderList(categoryId: String): Flow<FirebaseResult<MutableList<OrderDTO>>> {
        return fetchFirebaseDataFlow(
            fetcher = { dataSource.getOrderList(categoryId) },
            mapper = { OrderDTO(it.id, it.name) }
        )
    }
}