package com.kitching.domain.repository

import com.kitching.data.dto.DepartmentDTO
import com.kitching.data.dto.StaffLevelDTO
import com.kitching.data.firebase.FirebaseResult
import kotlinx.coroutines.flow.Flow

interface StaffLevelRepository {
    fun getStaffLevels(departmentId: String): Flow<FirebaseResult<List<StaffLevelDTO>>>

    fun createStaffLevel(departmentId: String, name: String): Flow<FirebaseResult<Boolean>>

    fun updateStaffLevel(staffLevelId: String, name: String): Flow<FirebaseResult<Boolean>>

    fun deleteStaffLevel(staffLevelId: String): Flow<FirebaseResult<Boolean>>
}