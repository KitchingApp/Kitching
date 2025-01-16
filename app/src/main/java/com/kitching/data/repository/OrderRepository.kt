package com.kitching.data.repository

import com.kitching.data.dto.OrderCategoryDTO
import com.kitching.data.dto.OrderDTO
import com.kitching.data.firebase.FireStoreDataSource
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.firebase.fetchFirebaseDataFlow
import kotlinx.coroutines.flow.Flow

class OrderRepository(private val dataSource: FireStoreDataSource = FireStoreDataSource()) {
    /** OrderCategory */
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

    suspend fun createOrderCategory(teamId: String, categoryName: String, color: String): Flow<FirebaseResult<Boolean>> {
        return fetchFirebaseDataFlow(dataSource.createOrderCategory(teamId, categoryName, color))
    }

    suspend fun deleteOrderCategory(categoryId: String): Flow<FirebaseResult<Boolean>> {
        return fetchFirebaseDataFlow(dataSource.deleteOrderCategory(categoryId))
    }

    suspend fun updateOrderCategory(categoryId: String, categoryName: String, color: String): Flow<FirebaseResult<Boolean>> {
        return fetchFirebaseDataFlow(dataSource.updateOrderCategory(categoryId, categoryName, color))

    }

    /** Order */

    suspend fun getOrderList(categoryId: String): Flow<FirebaseResult<MutableList<OrderDTO>>> {
        return fetchFirebaseDataFlow(
            fetcher = { dataSource.getOrderList(categoryId) },
            mapper = { OrderDTO(categoryId, it.id, it.name) }
        )
    }

    suspend fun createOrder(categoryId: String, orderName: String): Flow<FirebaseResult<Boolean>> {
        return fetchFirebaseDataFlow(dataSource.createOrder(categoryId, orderName))
    }

    suspend fun deleteOrder(orderId: String): Flow<FirebaseResult<Boolean>> {
        return fetchFirebaseDataFlow(dataSource.deleteOrder(orderId))
    }
}