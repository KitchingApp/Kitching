package com.kitching.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.kitching.common.COLLECTION_ORDER
import com.kitching.domain.datasource.OrderDataSource
import com.kitching.domain.entities.Order
import kotlinx.coroutines.tasks.await

class OrderDataSourceImpl(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()): OrderDataSource {
    override suspend fun getOrderList(categoryId: String): List<Order> {
        return db.collection(COLLECTION_ORDER).whereEqualTo("categoryId", categoryId).get().await().toObjects(Order::class.java)
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