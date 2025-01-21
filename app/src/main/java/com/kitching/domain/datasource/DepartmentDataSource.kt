package com.kitching.domain.datasource

import com.kitching.domain.entities.Department

interface DepartmentDataSource {
    suspend fun getDepartment(departmentId: String): Result<Department>

    suspend fun getDepartments(teamId: String): Result<List<Department>>

    suspend fun createDepartment(teamId: String, name: String, color: String): Boolean

    suspend fun updateDepartment(departmentId: String, name: String, color: String): Boolean

    suspend fun deleteDepartment(departmentId: String): Boolean
}