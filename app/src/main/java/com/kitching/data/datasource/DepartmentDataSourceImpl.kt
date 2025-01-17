package com.kitching.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.kitching.common.COLLECTION_DEPARTMENT
import com.kitching.common.COLLECTION_STAFF_LEVEL
import com.kitching.domain.datasource.DepartmentDataSource
import com.kitching.domain.entities.Department
import com.kitching.domain.entities.StaffLevel
import kotlinx.coroutines.tasks.await

class DepartmentDataSourceImpl(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()):
    DepartmentDataSource {
    override suspend fun getDepartments(teamId: String): Result<List<Department>> {
        return runCatching {
            db.collection(COLLECTION_DEPARTMENT).whereEqualTo("teamId", teamId).get().await()
                .toObjects(Department::class.java)
        }
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
            db.collection(COLLECTION_DEPARTMENT).document(departmentId).update("name", name, "color", color).await()
        }.isSuccess
    }

    override suspend fun deleteDepartment(departmentId: String): Boolean {
        return runCatching {
            db.collection(COLLECTION_DEPARTMENT).document(departmentId).delete().await()
        }.isSuccess
    }

    override suspend fun getStaffLevels(departmentId: String): Result<List<StaffLevel>> {
        return runCatching {
            db.collection(COLLECTION_STAFF_LEVEL).whereEqualTo("departmentId", departmentId).get().await()
                .toObjects(StaffLevel::class.java)
        }
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
            db.collection(COLLECTION_STAFF_LEVEL).document(staffLevelId).update("name", name).await()
        }.isSuccess
    }

    override suspend fun deleteStaffLevel(staffLevelId: String): Boolean {
        return runCatching {
            db.collection(COLLECTION_STAFF_LEVEL).document(staffLevelId).delete().await()
        }.isSuccess
    }
}