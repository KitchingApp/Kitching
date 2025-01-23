package com.kitching.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.kitching.common.COLLECTION_ORDER_CATEGORY
import com.kitching.domain.datasource.OrderCategoryDataSource
import com.kitching.domain.entities.OrderCategory
import kotlinx.coroutines.tasks.await

class OrderCategoryDataSourceImpl(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()): OrderCategoryDataSource {
    override suspend fun getOrderCategories(teamId: String): List<OrderCategory> {
            return db.collection(COLLECTION_ORDER_CATEGORY).whereEqualTo("teamId", teamId).get().await().toObjects(OrderCategory::class.java)
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
}