package com.kitching.domain.repository

import com.kitching.data.dto.DepartmentDTO
import com.kitching.data.dto.StaffLevelDTO
import com.kitching.data.firebase.FirebaseResult
import kotlinx.coroutines.flow.Flow

interface DepartmentRepository {
    fun getDepartments(teamId: String): Flow<FirebaseResult<List<DepartmentDTO>>>

    fun createDepartment(teamId: String, name: String, color: String): Flow<FirebaseResult<Boolean>>

    fun updateDepartment(departmentId: String, name: String, color: String): Flow<FirebaseResult<Boolean>>

    fun deleteDepartment(departmentId: String): Flow<FirebaseResult<Boolean>>

    fun getStaffLevels(departmentId: String): Flow<FirebaseResult<List<StaffLevelDTO>>>

    fun createStaffLevel(departmentId: String, name: String): Flow<FirebaseResult<Boolean>>

    fun updateStaffLevel(staffLevelId: String, name: String): Flow<FirebaseResult<Boolean>>

    fun deleteStaffLevel(staffLevelId: String): Flow<FirebaseResult<Boolean>>
}