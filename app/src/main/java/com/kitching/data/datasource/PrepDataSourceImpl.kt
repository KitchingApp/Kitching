package com.kitching.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.kitching.common.COLLECTION_PREP
import com.kitching.common.COLLECTION_PREP_CATEGORY
import com.kitching.domain.datasource.PrepDataSource
import com.kitching.domain.entities.Prep
import com.kitching.domain.entities.PrepCategory
import kotlinx.coroutines.tasks.await

class PrepDataSourceImpl(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) : PrepDataSource {

    /** PrepCategory */

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

    override suspend fun getPrepCategory(teamId: String): List<PrepCategory> {
        val result = runCatching {
            db.collection(COLLECTION_PREP_CATEGORY).whereEqualTo("teamId", teamId).get().await()
        }

        return if (result.isSuccess) {
            result.getOrNull()?.toObjects(PrepCategory::class.java) as MutableList<PrepCategory>
        } else {
            mutableListOf()
        }
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

    /** PrepList */

    override suspend fun createPrepList(categoryId: String, name: String): Boolean {
        return runCatching {
            val prepWithOutId = Prep(
                categoryId = categoryId,
                id = "",
                name = name,
                recipeId = ""
            )

            val document = db.collection(COLLECTION_PREP).add(prepWithOutId).await()

            db.collection(COLLECTION_PREP).document(document.id).update("id", document.id).await()
        }.isSuccess
    }

    override suspend fun getPrepList(categoryId: String): List<Prep> {
        val result = runCatching {
            db.collection(COLLECTION_PREP).whereEqualTo("categoryId", categoryId).get().await()
        }
        return if (result.isSuccess) {
            result.getOrNull()?.toObjects(Prep::class.java) as MutableList<Prep>
        } else {
            mutableListOf()
        }
    }

    override suspend fun updatePrepList(prepId: String, name: String): Boolean {
        return runCatching {
            db.collection(COLLECTION_PREP).document(prepId).update("name", name).await()
        }.isSuccess
    }

    override suspend fun deletePrepList(prepId: String): Boolean {
        return runCatching {
            db.collection(COLLECTION_PREP).document(prepId).delete().await()
        }.isSuccess
    }

}