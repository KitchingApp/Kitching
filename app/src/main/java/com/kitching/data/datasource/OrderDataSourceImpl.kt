package com.kitching.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.kitching.common.COLLECTION_ORDER
import com.kitching.common.COLLECTION_ORDER_CATEGORY
import com.kitching.domain.datasource.OrderDataSource
import com.kitching.domain.entities.Order
import com.kitching.domain.entities.OrderCategory
import kotlinx.coroutines.tasks.await

class OrderDataSourceImpl(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()): OrderDataSource {
    override suspend fun getOrderCategory(teamId: String): Result<List<OrderCategory>> {
        return runCatching {
            val orderCategories = db.collection(COLLECTION_ORDER_CATEGORY).whereEqualTo("teamId", teamId).get().await()

            orderCategories.toObjects(OrderCategory::class.java)
        }
    }

    override suspend fun createOrderCategory(teamId: String, categoryName: String, color: String): Boolean {
        return runCatching {
            db.collection(COLLECTION_ORDER_CATEGORY).add(
                OrderCategory(
                    id = "",
                    name = categoryName,
                    color = color,
                    teamId = teamId
                )
            ).await().apply {
                this.update("id", this.id).await()
            }
        }.isSuccess
    }

    override suspend fun deleteOrderCategory(categoryId: String): Boolean {
        return runCatching {
            db.collection(COLLECTION_ORDER_CATEGORY).document(categoryId).delete().await()
        }.isSuccess
    }

    override suspend fun updateOrderCategory(
        categoryId: String,
        categoryName: String,
        color: String
    ): Boolean {
        return runCatching {
            db.collection(COLLECTION_ORDER_CATEGORY).document(categoryId).update("name", categoryName, "color", color)
        }.isSuccess
    }

    override suspend fun getOrderList(categoryId: String): Result<List<Order>> {
        return runCatching {
            db.collection(COLLECTION_ORDER).whereEqualTo("categoryId", categoryId).get().await().toObjects(Order::class.java)
        }
    }

    override suspend fun createOrder(categoryId: String, name: String): Boolean {
        return runCatching {
            db.collection(COLLECTION_ORDER).add(
                Order(
                    id = "",
                    name = name,
                    categoryId = categoryId
                )
            ).await().apply {
                this.update("id", this.id).await()
            }
        }.isSuccess
    }

    override suspend fun updateOrder(orderId: String, name: String): Boolean {
        return runCatching {
            db.collection(COLLECTION_ORDER).document(orderId).update("name", name).await()
        }.isSuccess
    }

    override suspend fun deleteOrder(orderId: String): Boolean {
        return runCatching {
            db.collection(COLLECTION_ORDER).document(orderId).delete().await()
        }.isSuccess
    }
}