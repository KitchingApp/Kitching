package com.kitching.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.kitching.common.COLLECTION_DEPARTMENT
import com.kitching.domain.datasource.DepartmentDataSource
import com.kitching.domain.entities.Department
import kotlinx.coroutines.tasks.await

class DepartmentDataSourceImpl(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) :
    DepartmentDataSource {
    override suspend fun getDepartment(departmentId: String): Department? {
        return db.collection(COLLECTION_DEPARTMENT).document(departmentId).get().await().toObject(Department::class.java)
    }

    override suspend fun getDepartments(teamId: String): List<Department> {
        return db.collection(COLLECTION_DEPARTMENT).whereEqualTo("teamId", teamId).get().await()
            .toObjects(Department::class.java)
    }

    override suspend fun createDepartment(teamId: String, name: String, color: String): Boolean {
        return runCatching {
            db.collection(COLLECTION_DEPARTMENT).add(
                Department(
                    id = "",
                    teamId = teamId,
                    name = name,
                    color = color
                )
            ).await().apply {
                this.update("id", id).await()
            }
        }.isSuccess
    }

    override suspend fun updateDepartment(
        departmentId: String,
        name: String,
        color: String
    ): Boolean {
        return runCatching {
            db.collection(COLLECTION_DEPARTMENT).document(departmentId)
                .update("name", name, "color", color).await()
        }.isSuccess
    }

    override suspend fun deleteDepartment(departmentId: String): Boolean {
        return runCatching {
            db.collection(COLLECTION_DEPARTMENT).document(departmentId).delete().await()
        }.isSuccess
    }
}