package com.kitching.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.kitching.common.COLLECTION_PREP_CATEGORY
import com.kitching.domain.datasource.PrepCategoryDataSource
import com.kitching.domain.entities.PrepCategory
import kotlinx.coroutines.tasks.await

class PrepCategoryDataSourceImpl(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) : PrepCategoryDataSource {
    override suspend fun getPrepCategory(teamId: String): List<PrepCategory> {
        return db.collection(COLLECTION_PREP_CATEGORY).whereEqualTo("teamId", teamId).get().await().toObjects(PrepCategory::class.java)
    }

    override suspend fun createPrepCategory(
        teamId: String,
        categoryName: String,
        color: String,
    ): Boolean {
        val prepCategoryWithOutId = PrepCategory(
            id = "",
            teamId = teamId,
            name = categoryName,
            color = color
        )

        val result = runCatching {
            val document =
                db.collection(COLLECTION_PREP_CATEGORY).add(prepCategoryWithOutId).await()

            db.collection(COLLECTION_PREP_CATEGORY).document(document.id).update("id", document.id)
        }

        return result.isSuccess
    }

    override suspend fun updatePrepCategory(
        categoryId: String,
        categoryName: String,
        color: String,
    ): Boolean {
        return runCatching {
            db.collection(COLLECTION_PREP_CATEGORY).document(categoryId)
                .update("name", categoryName, "color", color)
        }.isSuccess
    }

    override suspend fun deletePrepCategory(prepCategoryId: String): Boolean {
        return runCatching {
            db.collection(COLLECTION_PREP_CATEGORY).document(prepCategoryId).delete().await()
        }.isSuccess
    }
}