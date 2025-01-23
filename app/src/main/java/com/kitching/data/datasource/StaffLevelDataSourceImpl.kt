package com.kitching.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.kitching.common.COLLECTION_STAFF_LEVEL
import com.kitching.domain.datasource.StaffLevelDataSource
import com.kitching.domain.entities.StaffLevel
import kotlinx.coroutines.tasks.await

class StaffLevelDataSourceImpl(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) :
    StaffLevelDataSource {
    override suspend fun getStaffLevel(staffLevelId: String): StaffLevel? {
        return db.collection(COLLECTION_STAFF_LEVEL).document(staffLevelId).get().await().toObject(StaffLevel::class.java)
    }

    override suspend fun getStaffLevels(departmentId: String): List<StaffLevel> {
        return db.collection(COLLECTION_STAFF_LEVEL).whereEqualTo("departmentId", departmentId)
            .get().await()
            .toObjects(StaffLevel::class.java)
    }

    override suspend fun createStaffLevel(departmentId: String, staffLevelName: String): Boolean {
        return runCatching {
            db.collection(COLLECTION_STAFF_LEVEL).add(
                StaffLevel(
                    id = "",
                    departmentId = departmentId,
                    name = staffLevelName
                )
            ).await().apply {
                this.update("id", id).await()
            }
        }.isSuccess
    }

    override suspend fun updateStaffLevel(staffLevelId: String, name: String): Boolean {
        return runCatching {
            db.collection(COLLECTION_STAFF_LEVEL).document(staffLevelId).update("name", name)
                .await()
        }.isSuccess
    }

    override suspend fun deleteStaffLevel(staffLevelId: String): Boolean {
        return runCatching {
            db.collection(COLLECTION_STAFF_LEVEL).document(staffLevelId).delete().await()
        }.isSuccess
    }
}