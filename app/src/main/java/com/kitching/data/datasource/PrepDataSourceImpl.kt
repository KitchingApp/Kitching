package com.kitching.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.kitching.common.COLLECTION_PREP
import com.kitching.domain.datasource.PrepDataSource
import com.kitching.domain.entities.Prep
import kotlinx.coroutines.tasks.await

class PrepDataSourceImpl(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) : PrepDataSource {
    override suspend fun getPrepList(categoryId: String): Result<List<Prep>> {
        return runCatching {
            db.collection(COLLECTION_PREP).whereEqualTo("categoryId", categoryId).get().await().toObjects(Prep::class.java)
        }
    }

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