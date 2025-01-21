package com.kitching.domain.datasource

import com.kitching.domain.entities.StaffLevel

interface StaffLevelDataSource {
    suspend fun getStaffLevel(staffLevelId: String): Result<StaffLevel>

    suspend fun getStaffLevels(departmentId: String): Result<List<StaffLevel>>

    suspend fun createStaffLevel(departmentId: String, staffLevelName: String): Boolean

    suspend fun updateStaffLevel(staffLevelId: String, name: String): Boolean

    suspend fun deleteStaffLevel(staffLevelId: String): Boolean
}