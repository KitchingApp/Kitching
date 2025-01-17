package com.kitching.domain.datasource

import com.kitching.domain.entities.Department
import com.kitching.domain.entities.StaffLevel

interface DepartmentDataSource {
    suspend fun getDepartments(teamId: String): Result<List<Department>>

    suspend fun createDepartment(teamId: String, name: String, color: String): Boolean

    suspend fun updateDepartment(departmentId: String, name: String, color: String): Boolean

    suspend fun deleteDepartment(departmentId: String): Boolean

    suspend fun getStaffLevels(departmentId: String): Result<List<StaffLevel>>

    suspend fun createStaffLevel(departmentId: String, staffLevelName: String): Boolean

    suspend fun updateStaffLevel(staffLevelId: String, name: String): Boolean

    suspend fun deleteStaffLevel(staffLevelId: String): Boolean
}