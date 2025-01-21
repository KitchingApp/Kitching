package com.kitching.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.kitching.common.COLLECTION_USER
import com.kitching.domain.datasource.UserDataSource
import com.kitching.domain.entities.User
import kotlinx.coroutines.tasks.await

class UserDataSourceImpl(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()): UserDataSource {
    override suspend fun getUser(userId: String): Result<User> {
        return runCatching {
            db.collection(COLLECTION_USER).whereEqualTo("id", userId).get().await()
                .toObjects(User::class.java).first()
        }
    }

    override suspend fun checkAndSaveUser(
        userId: String,
        userName: String,
        userImage: String
    ): Boolean {
        return runCatching {
            val userDoc = db.collection(COLLECTION_USER).document(userId).get().await().toObject(User::class.java)
            if(userDoc == null) {
                db.collection(COLLECTION_USER).add(User(id = userId, userName = userName, userImage = userImage)).await()
            }
        }.isSuccess
    }
}